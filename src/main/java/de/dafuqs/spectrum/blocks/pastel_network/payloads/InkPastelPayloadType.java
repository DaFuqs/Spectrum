package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

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
	public void tick(PastelTransmissionLogic logic, PastelNetwork.NodePriority priority) {
		Set<PastelNodeBlockEntity> nodes = logic.getLoadedNodes(PastelNodeType.SENDER);
		if (nodes.isEmpty()) return;
		
		List<InkStorageBlockEntity<?>> blockEntities = new ArrayList<>(nodes.size());
		List<InkStorage> inkStorages = new ArrayList<>(nodes.size());
		for (PastelNodeBlockEntity node : nodes) {
			InkStorageBlockEntity<?> storage = getConnectedInkStorage(node);
			if (storage != null) {
				blockEntities.add(storage);
				inkStorages.add(storage.getEnergyStorage());
			}
		}
		if (blockEntities.isEmpty()) return;
		
		InkStorage.equalizeInk(inkStorages);
		for(InkStorageBlockEntity<?> s : blockEntities) {
			s.setInkDirty();
		}
	}
	
}
