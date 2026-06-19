package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InkPastelPayloadType extends PastelPayloadType {
	
	public InkPastelPayloadType() {
		super(SpectrumCommon.locate("ink"), SpectrumPastelPayloads.INK.get());
	}
	
	public static @Nullable InkCapability getConnectedInkStorage(PastelNodeBlockEntity pastelNodeBlockEntity) {
		BlockState state = pastelNodeBlockEntity.getBlockState();
		if (!(state.getBlock() instanceof PastelNodeBlock)) {
			return null;
		}
		Direction direction = state.getValue(PastelNodeBlock.FACING);
		return pastelNodeBlockEntity.getLevel().getCapability(InkCapabilities.BLOCK, pastelNodeBlockEntity.getBlockPos().relative(direction.getOpposite()), null);
	}
	
	@Override
	public void tick(PastelTransmissionLogic logic, PastelNetwork.NodePriority priority) {
		Set<PastelNodeBlockEntity> nodes = logic.getLoadedNodes(SpectrumPastelPayloadTypes.INK);
		if (nodes.size() < 2) return;
		
		List<InkCapability> targets = new ArrayList<>(nodes.size());
		for (PastelNodeBlockEntity node : nodes) {
			InkCapability inkCapability = getConnectedInkStorage(node);
			if (inkCapability != null) {
				targets.add(inkCapability);
			}
		}
		
		if (targets.size() < 2) {
			return;
		}
		
		InkTransferHelper.equalizeInk(targets);
	}
	
}
