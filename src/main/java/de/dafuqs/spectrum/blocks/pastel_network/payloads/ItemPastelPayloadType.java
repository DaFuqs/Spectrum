package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.pastel_network.network.PastelTransmission;
import de.dafuqs.spectrum.blocks.pastel_network.network.PastelTransmissionLogic;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.networking.s2c_payloads.PastelNodeStatusUpdatePayload;
import net.minecraft.core.*;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.*;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ItemPastelPayloadType extends PastelPayloadType {
	
	public ItemPastelPayloadType() {
		super(SpectrumCommon.locate("item"), SpectrumPastelPayloads.ITEM.get());
	}
	
	public static @Nullable IItemHandler getConnectedItemStorage(PastelNodeBlockEntity pastelNodeBlockEntity) {
		BlockState state = pastelNodeBlockEntity.getBlockState();
		if (!(state.getBlock() instanceof PastelNodeBlock)) {
			return null;
		}
		Direction direction = state.getValue(PastelNodeBlock.FACING);
		return pastelNodeBlockEntity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, pastelNodeBlockEntity.getBlockPos().relative(direction.getOpposite()), direction);
	}
	
	@Override
	public void tryTransferToType(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, PastelNodeType type, PastelTransmissionLogic.TransferMode transferMode) {
		IItemHandler sourceHandler = getConnectedItemStorage(sourceNode);
		if (sourceHandler == null) {
			return;
		}
		
		for (PastelNodeBlockEntity destinationNode : logic.getLoadedNodes(type)) {
			if (!destinationNode.canTransfer()) {
				continue;
			}
			
			IItemHandler destinationHandler = getConnectedItemStorage(destinationNode);
			if (destinationHandler != null) {
				boolean success = transferBetween(logic, sourceNode, sourceHandler, destinationNode, destinationHandler, transferMode);
				if (success && transferMode != PastelTransmissionLogic.TransferMode.PULL) {
					return;
				}
			}
		}
	}
	
	private boolean transferBetween(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, IItemHandler sourceStorage, PastelNodeBlockEntity destinationNode, IItemHandler destinationStorage, PastelTransmissionLogic.TransferMode transferMode) {
		// check how much room is in the target inventory
		long totalAvailableStorage = -destinationNode.getItemCountUnderway();
		for (int d = 0; d < destinationStorage.getSlots(); d++) {
			ItemStack stack = destinationStorage.getStackInSlot(d);
			
			if (stack.isEmpty()) {
				totalAvailableStorage += destinationStorage.getSlotLimit(d);
			} else {
				totalAvailableStorage += Math.min(destinationStorage.getSlotLimit(d), stack.getMaxStackSize()) - stack.getCount();
			}
		}
		
		if (totalAvailableStorage <= 0)
			return false;
		
		Predicate<ItemStack> filter = sourceNode.getTransferFilterTo(destinationNode);
		Map<ItemStack, Long> proposals = new HashMap<>();
		for (int s = 0; s < sourceStorage.getSlots(); s++) {
			ItemStack stack = sourceStorage.extractItem(s, PastelTransmissionLogic.DEFAULT_MAX_TRANSFER_AMOUNT, true);
			
			if (stack.isEmpty())
				continue;
			if (!filter.test(stack))
				continue;
			
			proposals.put(stack, proposals.getOrDefault(stack, 0L) + stack.getCount());
		}
		
		for (ItemStack stack : proposals.keySet()) {
			long proposedAmount = Math.min(Math.min(proposals.get(stack), sourceNode.getMaxTransferredAmount()), totalAvailableStorage);
			if (proposedAmount == 0)
				continue;
			
			ItemStack proposedStack = stack.copyWithCount((int) proposedAmount);
			int simulatedAmount = (int) (proposedAmount - ItemHandlerHelper.insertItemStacked(destinationStorage, proposedStack, true).getCount());
			Tuple<Integer, List<ItemStack>> matchingStacks = InventoryHelper.getStackCountInInventory(proposedStack, sourceStorage, simulatedAmount);
			
			if (matchingStacks.getA() == 0)
				continue;
			
			GraphPath<BlockPos, DefaultEdge> graphPath = logic.getPath(sourceNode, destinationNode);
			if (graphPath == null) {
				return false;
			}
			
			PastelPayload payload = new ItemPastelPayload(proposedStack.copyWithCount(simulatedAmount));
			PastelTransmission transmission = new PastelTransmission(graphPath.getVertexList(), payload, sourceNode.getTransferTime());
			int extracted = 0;
			for (int i = 0; i < sourceStorage.getSlots(); i++) {
				if (ItemStack.isSameItemSameComponents(proposedStack, sourceStorage.getStackInSlot(i)))
					extracted += sourceStorage.extractItem(i, simulatedAmount - extracted, false).getCount();
				if (extracted == simulatedAmount)
					break;
			}
			
			logic.addTransmission(sourceNode, destinationNode, transferMode, transmission);
			destinationNode.addItemCountUnderway(simulatedAmount);
			return true;
		}
		return false;
	}
	
}
