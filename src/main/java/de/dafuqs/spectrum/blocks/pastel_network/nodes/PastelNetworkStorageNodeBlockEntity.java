package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PastelNetworkStorageNodeBlockEntity extends PastelNetworkNodeBlockEntity {
	
	public PastelNetworkStorageNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.STORAGE_NODE, blockPos, blockState);
	}
	
}