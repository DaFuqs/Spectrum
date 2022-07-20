package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PastelNetworkPullerNodeBlockEntity extends PastelNetworkNodeBlockEntity {
	
	public PastelNetworkPullerNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.PULLER_NODE, blockPos, blockState);
	}
	
}