package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class BlackslagBlock extends RotatedPillarBlock implements BonemealableBlock {
	
	public static final MapCodec<BlackslagBlock> CODEC = simpleCodec(BlackslagBlock::new);
	
	public BlackslagBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends BlackslagBlock> codec() {
		return CODEC;
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		for (BlockPos currPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
			BlockState currState = world.getBlockState(currPos);
			if (currState.is(SpectrumBlockTags.SPREADS_TO_BLACKSLAG) || currState.is(SpectrumBlockTags.OVERGROWN)) {
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
		List<BlockState> nextStates = new ArrayList<>();
		
		// search for all valid neighboring blocks and choose a weighted random one
		for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
			BlockState blockState = world.getBlockState(blockPos);
			if (blockState.is(SpectrumBlockTags.OVERGROWN)) {
				overgrownBlockNext = true;
			} else if (blockState.is(SpectrumBlockTags.SPREADS_TO_BLACKSLAG)) {
				nextStates.add(blockState);
			}
		}
		
		if (overgrownBlockNext) {
			if (nextStates.isEmpty() || random.nextInt(nextStates.size() + 1) == 0) {
				world.setBlockAndUpdate(pos, SpectrumBlocks.OVERGROWN_BLACKSLAG.defaultBlockState());
				return;
			}
		}
		
		if (nextStates.isEmpty()) {
			return;
		}
		
		Collections.shuffle(nextStates);
		world.setBlockAndUpdate(pos, nextStates.getFirst());
	}
}
