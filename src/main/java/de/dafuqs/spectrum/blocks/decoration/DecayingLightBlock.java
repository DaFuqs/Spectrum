package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.Random;

public class DecayingLightBlock extends WandLightBlock {
	
	public DecayingLightBlock(Settings settings) {
		super(settings);
	}
	
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		int light = state.get(LightBlock.LEVEL_15);
		if (light < 2) {
			if (state.get(WATERLOGGED)) {
				world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
			} else {
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			}
		} else {
			world.setBlockState(pos, state.with(LightBlock.LEVEL_15, light - 1), 3);
		}
	}
	
}
