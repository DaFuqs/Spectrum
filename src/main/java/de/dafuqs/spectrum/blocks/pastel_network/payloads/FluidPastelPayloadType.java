package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

public class FluidPastelPayloadType extends PastelPayloadType {
	
	public FluidPastelPayloadType() {
		super(SpectrumCommon.locate("fluid"), SpectrumPastelPayloads.FLUID.get());
	}
	
	public static @Nullable IFluidHandler getConnectedFluidStorage(PastelNodeBlockEntity pastelNodeBlockEntity) {
		BlockState state = pastelNodeBlockEntity.getBlockState();
		if (!(state.getBlock() instanceof PastelNodeBlock)) {
			return null;
		}
		Direction direction = state.getValue(PastelNodeBlock.FACING);
		return pastelNodeBlockEntity.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, pastelNodeBlockEntity.getBlockPos().relative(direction.getOpposite()), direction);
	}
	
	@Override
	public DeferredHolder<PastelPayloadType, ?> getPayloadType() {
		return SpectrumPastelPayloadTypes.FLUID;
	}
	
	@Override
	public void tryTransferToType(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, PastelNodeType type, PastelTransmissionLogic.TransferMode transferMode) {
		IFluidHandler sourceHandler = getConnectedFluidStorage(sourceNode);
		if (sourceHandler == null) {
			return;
		}
		
		for (PastelNodeBlockEntity destinationNode : logic.getLoadedNodes(getPayloadType(), type)) {
			if (!destinationNode.isEnabled() || !sourceNode.cooldownExceededTo(destinationNode)) {
				continue;
			}
			
			IFluidHandler destinationHandler = getConnectedFluidStorage(destinationNode);
			if (destinationHandler != null) {
				boolean success = transferBetween(logic, sourceNode, sourceHandler, destinationNode, destinationHandler, transferMode);
				if (success && transferMode != PastelTransmissionLogic.TransferMode.PULL) {
					return;
				}
			}
		}
	}
	
	private boolean transferBetween(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, IFluidHandler sourceHandler, PastelNodeBlockEntity destinationNode, IFluidHandler destinationHandler, PastelTransmissionLogic.TransferMode transferMode) {
		long underwayCount = destinationNode.getUnderway(getPayloadType().getKey());
		for(int sourceTankId = 0; sourceTankId < sourceHandler.getTanks(); sourceTankId++) {
			FluidStack sourceFluid = sourceHandler.getFluidInTank(sourceTankId);
			if (sourceFluid.isEmpty()) {
				continue;
			}
			
			int transferLimit = Math.max(sourceNode.getMaxTransferredAmount(), destinationNode.getMaxTransferredAmount()) * 50;
			FluidStack proposedStack = new FluidStack(sourceFluid.getFluid(), transferLimit + (int) underwayCount);
			int fluidSpace = destinationHandler.fill(proposedStack, IFluidHandler.FluidAction.SIMULATE);
			int amountToSend = Math.min(transferLimit, fluidSpace);
			if(amountToSend < 1) {
				continue;
			}
			
			// Find a valid path from source to target
			// there always should be one - but better safe than sorry
			GraphPath<BlockPos, DefaultEdge> graphPath = logic.getPath(sourceNode, destinationNode);
			if (graphPath == null) {
				return false;
			}
			
			FluidStack drainedAmount = sourceHandler.drain(sourceFluid.copyWithAmount(amountToSend), IFluidHandler.FluidAction.EXECUTE);
			PastelPayload payload = new FluidPastelPayload(drainedAmount);
			PastelTransmission transmission = new PastelTransmission(graphPath.getVertexList(), payload, sourceNode.getTransferTime());
			logic.addTransmission(sourceNode, destinationNode, transferMode, transmission);
			destinationNode.addUnderway(getPayloadType().getKey(), drainedAmount.getAmount());
			
			return true;
		}
		
		return false;
	}
	
}
