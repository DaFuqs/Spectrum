package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;

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
	public void tryTransferToType(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, PastelNodeType type, PastelTransmissionLogic.TransferMode transferMode) {
		IFluidHandler sourceHandler = getConnectedFluidStorage(sourceNode);
		if (sourceHandler == null) {
			return;
		}
		
		for (PastelNodeBlockEntity destinationNode : logic.getLoadedNodes(type)) {
			if (!destinationNode.canTransfer()) {
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
		for(int sourceTankId = 0; sourceTankId < sourceHandler.getTanks(); sourceTankId++) {
			FluidStack sourceFluid = sourceHandler.getFluidInTank(sourceTankId);
			if (sourceFluid.isEmpty()) {
				continue;
			}
			
			for(int destinationTankId = 0; destinationTankId < destinationHandler.getTanks(); destinationTankId++) {
				FluidStack destinationFluid = destinationHandler.getFluidInTank(destinationTankId);
				if (!destinationFluid.isEmpty() && !destinationFluid.is(sourceFluid.getFluid())) {
					continue;
				}
				
				GraphPath<BlockPos, DefaultEdge> graphPath = logic.getPath(sourceNode, destinationNode);
				if (graphPath == null) {
					continue;
				}
				
				int roomAtDestination = destinationHandler.getTankCapacity(destinationTankId) - destinationFluid.getAmount();
				int transmittedAmount = Math.min(Math.min(sourceFluid.getAmount(), roomAtDestination), sourceNode.getMaxTransferredAmount() * 20);
				FluidStack drainedStack = sourceHandler.drain(sourceFluid.copyWithAmount(transmittedAmount), IFluidHandler.FluidAction.EXECUTE);
				if(drainedStack.isEmpty()) {
					continue;
				}
				
				PastelPayload payload = new FluidPastelPayload(drainedStack);
				PastelTransmission transmission = new PastelTransmission(graphPath.getVertexList(), payload, sourceNode.getTransferTime());
				logic.addTransmission(sourceNode, destinationNode, transferMode, transmission);
				
				return true;
			}
		}
		
		return false;
	}
	
}
