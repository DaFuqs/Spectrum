package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PastelNetworkConnectionNode extends PastelNetworkNodeBlockEntity {
	
	public PastelNetworkConnectionNode(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.CONNECTION_NODE, blockPos, blockState);
	}
	
}