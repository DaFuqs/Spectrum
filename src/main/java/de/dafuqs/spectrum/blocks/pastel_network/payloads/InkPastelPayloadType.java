package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.function.*;

public class InkPastelPayloadType extends PastelPayloadType {
	
	public InkPastelPayloadType() {
		super(SpectrumCommon.locate("ink"), SpectrumPastelPayloads.INK.get());
	}
	
	public static @Nullable InkStorageBlockEntity<?> getConnectedInkStorage(PastelNodeBlockEntity pastelNodeBlockEntity) {
		BlockState state = pastelNodeBlockEntity.getBlockState();
		if (!(state.getBlock() instanceof PastelNodeBlock)) {
			return null;
		}
		Direction direction = state.getValue(PastelNodeBlock.FACING);
		BlockEntity be = pastelNodeBlockEntity.getLevel().getBlockEntity(pastelNodeBlockEntity.getBlockPos().relative(direction.getOpposite()));
		if(be instanceof InkStorageBlockEntity<?> inkStorageBlockEntity) {
			return inkStorageBlockEntity;
		}
		return null;
	}
	
	@Override
	public void tick(PastelTransmissionLogic logic) {
		for (PastelNodeBlockEntity sourceNode : logic.getLoadedNodes(PastelNodeType.SENDER)) {
			for(Supplier<? extends PastelPayloadType> payloadType : sourceNode.getSupportedPayloads()) {
				if(payloadType.get() != this) {
					continue;
				}
				payloadType.get().tryTransferToType(logic, sourceNode, PastelNodeType.SENDER, PastelTransmissionLogic.TransferMode.PUSH);
			}
		}
	}
	
	@Override
	public void tryTransferToType(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, PastelNodeType type, PastelTransmissionLogic.TransferMode transferMode) {
		@Nullable InkStorageBlockEntity<?> sourceHandler = getConnectedInkStorage(sourceNode);
		if (sourceHandler == null) {
			return;
		}
		
		for (PastelNodeBlockEntity destinationNode : logic.getLoadedNodes(type)) {
			if (!destinationNode.canTransfer()) {
				continue;
			}
			
			@Nullable InkStorageBlockEntity<?> destinationHandler = getConnectedInkStorage(destinationNode);
			if (destinationHandler != null) {
				boolean success = transferBetween(logic, sourceNode, sourceHandler, destinationNode, destinationHandler, transferMode);
				if (success && transferMode != PastelTransmissionLogic.TransferMode.PULL) {
					return;
				}
			}
		}
	}
	
	private boolean transferBetween(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, InkStorageBlockEntity<?> sourceHandler, PastelNodeBlockEntity destinationNode, InkStorageBlockEntity<?> destinationHandler, PastelTransmissionLogic.TransferMode transferMode) {
		InkStorage sourceStorage = sourceHandler.getEnergyStorage();
		InkStorage destinationStorage = destinationHandler.getEnergyStorage();
		for(Map.Entry<InkColor, Long> sourceEntry : sourceStorage.getEnergy().entrySet()) {
			GraphPath<BlockPos, DefaultEdge> graphPath = logic.getPath(sourceNode, destinationNode);
			if (graphPath == null) {
				continue;
			}
			
			InkColor sourceColor = sourceEntry.getKey();
			
			long roomAtDestination = destinationStorage.getRoom(sourceColor);
			List<InkAmount> transmittedAmounts = new  ArrayList<>();
			if(roomAtDestination > 0) {
				long transmittedAmount = Math.min(Math.min(sourceEntry.getValue(), roomAtDestination), sourceNode.getMaxTransferredAmount() * 20L);
				if(transmittedAmount > 0) {
					sourceStorage.addEnergy(sourceEntry.getKey(), -transmittedAmount);
					transmittedAmounts.add(new InkAmount(sourceColor, transmittedAmount));
					return true;
				}
			}
			
			if(!transmittedAmounts.isEmpty()) {
				PastelTransmission transmission = new PastelTransmission(graphPath.getVertexList(), new InkPastelPayload(transmittedAmounts), sourceNode.getTransferTime());
				logic.addTransmission(sourceNode, destinationNode, transferMode, transmission);
				sourceHandler.setInkDirty();
			}
		}
		return false;
	}
	
}
