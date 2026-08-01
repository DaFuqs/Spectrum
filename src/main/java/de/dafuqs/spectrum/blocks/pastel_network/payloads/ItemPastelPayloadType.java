package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.pastel_network.network.PastelTransmission;
import de.dafuqs.spectrum.blocks.pastel_network.network.PastelTransmissionLogic;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.core.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;

import java.util.function.Predicate;

public class ItemPastelPayloadType extends PastelPayloadType {
	
	public ItemPastelPayloadType() {
		super(SpectrumCommon.locate("item"), SpectrumPastelPayloads.ITEM.get());
	}
	
	@Override
	public DeferredHolder<PastelPayloadType, ?> getPayloadType() {
		return SpectrumPastelPayloadTypes.ITEM;
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
		
		for (PastelNodeBlockEntity destinationNode : logic.getLoadedNodes(getPayloadType(), type)) {
			if (!destinationNode.isEnabled() || !sourceNode.cooldownExceededTo(destinationNode)) {
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
		long underwayCount = destinationNode.getUnderway(getPayloadType().getKey());
		Predicate<ItemStack> filter = sourceNode.getTransferFilterTo(destinationNode);
		int transferLimit = Math.max(sourceNode.getMaxTransferredAmount(), destinationNode.getMaxTransferredAmount());
		for (int slotId = 0; slotId < sourceStorage.getSlots(); slotId++) {
			ItemStack stack = sourceStorage.extractItem(slotId, transferLimit, true);
			
			// is the stack valid?
			if (stack.isEmpty())
				continue;
			if (!filter.test(stack))
				continue;
			
			// how many items can be transferred from source to destination?
			long proposedAmount = stack.getCount();
			ItemStack proposedStack = stack.copyWithCount((int) (proposedAmount + underwayCount));
			int unableToInsertAmount = ItemHandlerHelper.insertItemStacked(destinationStorage, proposedStack, true).getCount();
			int amountToSend = stack.getCount() - unableToInsertAmount;
			if(amountToSend < 1) {
				continue;
			}
			
			// Find a valid path from source to target
			// there always should be one - but better safe than sorry
			GraphPath<BlockPos, DefaultEdge> graphPath = logic.getPath(sourceNode, destinationNode);
			if (graphPath == null) {
				return false;
			}
			
			// Remove from source inventory
			int extracted = 0;
			for (int sourceSlot = 0; sourceSlot < sourceStorage.getSlots(); sourceSlot++) {
				if (ItemStack.isSameItemSameComponents(stack, sourceStorage.getStackInSlot(sourceSlot)))
					extracted += sourceStorage.extractItem(sourceSlot, amountToSend - extracted, false).getCount();
				if (extracted == amountToSend)
					break;
			}
			
			// Send the items
			PastelPayload payload = new ItemPastelPayload(stack.copyWithCount(extracted));
			PastelTransmission transmission = new PastelTransmission(graphPath.getVertexList(), payload, sourceNode.getTransferTime());
			logic.addTransmission(sourceNode, destinationNode, transferMode, transmission);
			destinationNode.addUnderway(getPayloadType().getKey(), extracted);
			return true;
		}
		return false;
	}
	
}
