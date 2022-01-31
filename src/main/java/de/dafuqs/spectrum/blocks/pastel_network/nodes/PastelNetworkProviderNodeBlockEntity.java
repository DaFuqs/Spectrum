package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PastelNetworkProviderNodeBlockEntity extends PastelNetworkNodeBlockEntity {
	
	public PastelNetworkProviderNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.PROVIDER_NODE, blockPos, blockState);
	}
	
}