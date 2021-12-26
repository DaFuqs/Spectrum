package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class GlowBlock extends Block {
	
	public GlowBlock(Settings settings) {
		super(settings);
	}
	
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1.0F;
	}
	
	
}
