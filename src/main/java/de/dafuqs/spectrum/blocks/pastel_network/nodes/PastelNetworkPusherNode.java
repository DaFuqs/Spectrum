package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PastelNetworkPusherNode extends PastelNetworkNodeBlockEntity {
	
	public PastelNetworkPusherNode(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.PUSHER_NODE, blockPos, blockState);
	}
	
}