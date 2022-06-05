package de.dafuqs.spectrum.blocks.lava_sponge;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Queue;

public class LavaSpongeBlock extends SpongeBlock {
	
	public LavaSpongeBlock(Settings settings) {
		super(settings);
	}
	
	protected void update(World world, BlockPos pos) {
		if (this.absorbLava(world, pos)) {
			world.setBlockState(pos, SpectrumBlocks.WET_LAVA_SPONGE.getDefaultState(), 2);
			world.syncWorldEvent(2001, pos, Block.getRawIdFromState(Blocks.LAVA.getDefaultState()));
		}
	}
	
	private boolean absorbLava(World world, BlockPos pos) {
		Queue<Pair<BlockPos, Integer>> queue = Lists.newLinkedList();
		queue.add(new Pair<>(pos, 0));
		int i = 0;
		
		while (!queue.isEmpty()) {
			Pair<BlockPos, Integer> pair = queue.poll();
			BlockPos blockPos = pair.getLeft();
			int j = pair.getRight();
			
			for (Direction direction : Direction.values()) {
				BlockPos blockPos2 = blockPos.offset(direction);
				BlockState blockState = world.getBlockState(blockPos2);
				FluidState fluidState = world.getFluidState(blockPos2);
				if (fluidState.isIn(FluidTags.LAVA)) {
					if (blockState.getBlock() instanceof FluidDrainable && !((FluidDrainable) blockState.getBlock()).tryDrainFluid(world, blockPos2, blockState).isEmpty()) {
						++i;
						if (j < 6) {
							queue.add(new Pair<>(blockPos2, j + 1));
						}
					} else if (blockState.getBlock() instanceof FluidBlock) {
						world.setBlockState(blockPos2, Blocks.AIR.getDefaultState(), 3);
						++i;
						if (j < 6) {
							queue.add(new Pair<>(blockPos2, j + 1));
						}
					}
				}
			}
			
			if (i > 64) {
				break;
			}
		}
		
		return i > 0;
	}
	
}
