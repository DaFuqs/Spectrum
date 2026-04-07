package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

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
		if(!world.getBlockState(pos.above()).isAir()) {
			return false;
		}
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
		// search for all valid neighboring blocks and choose a weighted random one
		if(isValidBonemealTarget(world, pos, state)) {
			world.setBlockAndUpdate(pos, SpectrumBlocks.OVERGROWN_SLUSH.defaultBlockState());
		}
	}
	
}
