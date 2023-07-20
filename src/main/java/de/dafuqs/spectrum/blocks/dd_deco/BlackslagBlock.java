package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

import java.util.*;

public class BlackslagBlock extends PillarBlock implements Fertilizable {
	
	public BlackslagBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
		if (!world.getBlockState(pos.up()).isTransparent(world, pos)) {
			return false;
		}
		
		for (BlockPos currPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
			BlockState currState = world.getBlockState(currPos);
			if (currState.isIn(SpectrumBlockTags.SPREADS_TO_BLACKSLAG)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean canGrow(World world, net.minecraft.util.math.random.Random random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		List<BlockState> nextStates = new ArrayList<>();
		
		// search for all valid neighboring blocks and choose a weighted random one
		for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
			BlockState blockState = world.getBlockState(blockPos);
			if (blockState.isIn(SpectrumBlockTags.SPREADS_TO_BLACKSLAG)) {
				nextStates.add(blockState);
			}
		}
		
		if (nextStates.isEmpty()) {
			return;
		}
		
		Collections.shuffle(nextStates);
		world.setBlockState(pos, nextStates.get(0), Block.NOTIFY_ALL);
	}
}
