package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PastelNetworkPusherNodeBlockEntity extends PastelNetworkNodeBlockEntity {
	
	public PastelNetworkPusherNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.PUSHER_NODE, blockPos, blockState);
	}
	
}