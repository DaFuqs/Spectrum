package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class SlushBlock extends RotatedPillarBlock implements BonemealableBlock {
	
	public static final MapCodec<SlushBlock> CODEC = simpleCodec(SlushBlock::new);
	
	public SlushBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends SlushBlock> codec() {
		return CODEC;
	}
	
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		for (BlockPos currPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
			BlockState currState = world.getBlockState(currPos);
			if (currState.is(SpectrumBlockTags.OVERGROWN)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, net.minecraft.util.RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		boolean overgrownBlockNext = false;
		
		// search for all valid neighboring blocks and choose a weighted random one
		for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
			BlockState blockState = world.getBlockState(blockPos);
			if (blockState.is(SpectrumBlockTags.OVERGROWN)) {
				overgrownBlockNext = true;
			}
		}
		
		if (overgrownBlockNext) {
			world.setBlockAndUpdate(pos, SpectrumBlocks.OVERGROWN_SLUSH.defaultBlockState());
		}
	}
	
}
