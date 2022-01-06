package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PastelNetworkStorageNode extends PastelNetworkNodeBlockEntity {
	
	public PastelNetworkStorageNode(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.STORAGE_NODE, blockPos, blockState);
	}
	
}