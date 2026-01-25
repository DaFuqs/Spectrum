package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.*;

import java.util.*;

public class NephriteBlossomFeature extends Feature<NephriteBlossomFeatureConfig> {
	
	private static final List<Direction> VALID_DIRS = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
	
	public NephriteBlossomFeature(Codec<NephriteBlossomFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NephriteBlossomFeatureConfig> context) {
		var world = context.level();
		var origin = context.origin();
		var random = context.random();
		var floor = world.getBlockState(origin.below());
		var flowering = context.config().flowering();
		
		if (!floor.is(BlockTags.DIRT))
			return false;
		
		int stemHeight = Math.round(Mth.normal(random, 2, 1F) + 1);
		int leafHeight = Math.round(Mth.normal(random, 2.5F, 0.9F) + 1.85F);
		int maxY = origin.getY() + stemHeight + leafHeight;
		
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int i = origin.getY(); i < maxY; i++) {
			pos.set(origin.getX(), i, origin.getZ());
			if (!isReplaceable(world, pos)) {
				return false;
			}
		}
		
		generateStem(world, origin, stemHeight);
		genereateLeaves(world, origin, random, stemHeight, leafHeight, flowering);
		
		return true;
	}
	
	private void generateStem(LevelAccessor world, BlockPos origin, int stemHeight) {
		var stemPointer = origin.mutable();
		var topStem = false;
		
		for (int height = 0; height < stemHeight; height++) {
			
			if (height == 0) {
				this.setBlock(world, stemPointer, SpectrumBlocks.NEPHRITE_BLOSSOM_STEM.get().defaultBlockState());
				topStem = true;
			} else if (isReplaceable(world, stemPointer)) {
				this.setBlock(world, stemPointer, NephriteBlossomStemBlock.getStemVariant(topStem));
				topStem = !topStem;
			}
			stemPointer.move(0, 1, 0);
		}
	}
	
	private void genereateLeaves(LevelAccessor world, BlockPos origin, RandomSource random, int stemHeight, int leafHeight, boolean flowering) {
		var leafPointer = origin.mutable().move(0, stemHeight, 0);
		var leafDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
		
		for (int i = 0; i < leafHeight; i++) {
			for (int leaf = 0; leaf < 4; leaf++) {
				leafPointer.move(leafDirection);
				setBlockStateWithoutUpdatingNeighbors(world, leafPointer, getLeafState(random, flowering));
				leafDirection = cycleDirections(leafDirection, 1);
			}
			
			if (i != 0 && i != leafHeight - 1) {
				leafDirection = leafDirection.getOpposite();
				for (int leaf = 0; leaf < 4; leaf++) {
					leafPointer.move(leafDirection);
					setBlockStateWithoutUpdatingNeighbors(world, leafPointer, getLeafState(random, flowering));
					leafDirection = cycleDirections(leafDirection, 1);
				}
				leafDirection = leafDirection.getOpposite();
			}
			
			leafPointer.move(0, 1, 0);
			if (random.nextBoolean() ^ i % 3 == 0)
				leafDirection = cycleDirections(leafDirection, random.nextInt(3) - 1);
		}
	}
	
	private static void setBlockStateWithoutUpdatingNeighbors(LevelAccessor world, BlockPos pos, BlockState state) {
		if (isReplaceable(world, pos)) {
			world.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		}
	}
	
	private BlockState getLeafState(RandomSource random, boolean allowFlowering) {
		var state = SpectrumBlocks.NEPHRITE_BLOSSOM_LEAVES.get().defaultBlockState().setValue(NephriteBlossomLeavesBlock.DISTANCE, 1);
		if (!allowFlowering) {
			return state;
		}
		if (random.nextBoolean()) {
			return state.setValue(NephriteBlossomLeavesBlock.AGE, 1);
		}
		if (random.nextBoolean()) {
			return state.setValue(NephriteBlossomLeavesBlock.AGE, 2);
		}
		return state;
	}
	
	private Direction cycleDirections(Direction currentDir, int change) {
		return getDirectionFor(getDirectionOridinal(currentDir) + Math.abs(change));
	}
	
	private Direction getDirectionFor(int ordinal) {
		return VALID_DIRS.get(ordinal % 4);
	}
	
	private int getDirectionOridinal(Direction direction) {
		return VALID_DIRS.indexOf(direction);
	}
	
	private static boolean isReplaceable(LevelSimulatedReader world, BlockPos pos) {
		return world.isStateAtPosition(pos, (state) -> state.isAir() || state.is(BlockTags.REPLACEABLE_BY_TREES));
	}
	
}
