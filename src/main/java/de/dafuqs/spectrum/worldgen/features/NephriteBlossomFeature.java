package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

import java.util.*;

public class NephriteBlossomFeature extends Feature<NephriteBlossomFeatureConfig> {

    private static final List<Direction> VALID_DIRS = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    public NephriteBlossomFeature(Codec<NephriteBlossomFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<NephriteBlossomFeatureConfig> context) {
        var world = context.getWorld();
        var origin = context.getOrigin();
        var random = context.getRandom();
        var floor = world.getBlockState(origin.down());
        var flowering = context.getConfig().flowering();

        if (!floor.isIn(BlockTags.DIRT))
            return false;
		
		int stemHeight = Math.round(MathHelper.nextGaussian(random, 2, 1F) + 1);
		int leafHeight = Math.round(MathHelper.nextGaussian(random, 2.5F, 0.9F) + 1.85F);
		int maxY = origin.getY() + stemHeight + leafHeight;
		
		BlockPos.Mutable pos = new BlockPos.Mutable();
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

    private void generateStem(WorldAccess world, BlockPos origin, int stemHeight) {
        var stemPointer = origin.mutableCopy();
        var topStem = false;

        for (int height = 0; height < stemHeight; height++) {

            if (height == 0) {
                this.setBlockState(world, stemPointer, SpectrumBlocks.NEPHRITE_BLOSSOM_STEM.getDefaultState());
                topStem = true;
            } else if (isReplaceable(world, stemPointer)) {
                this.setBlockState(world, stemPointer, NephriteBlossomStemBlock.getStemVariant(topStem));
                topStem = !topStem;
            }
            stemPointer.move(0, 1, 0);
        }
    }
	
	private void genereateLeaves(WorldAccess world, BlockPos origin, Random random, int stemHeight, int leafHeight, boolean flowering) {
        var leafPointer = origin.mutableCopy().move(0, stemHeight, 0);
		var leafDirection = Direction.Type.HORIZONTAL.random(random);

        for (int i = 0; i < leafHeight; i++) {
            for(int leaf = 0; leaf < 4; leaf++) {
                leafPointer.move(leafDirection);
                setBlockStateWithoutUpdatingNeighbors(world, leafPointer, getLeafState(random, flowering));
                leafDirection = cycleDirections(leafDirection, 1);
            }

            if (i != 0 && i != leafHeight - 1) {
                leafDirection = leafDirection.getOpposite();
                for(int leaf = 0; leaf < 4; leaf++) {
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
    
    private static void setBlockStateWithoutUpdatingNeighbors(WorldAccess world, BlockPos pos, BlockState state) {
		if (isReplaceable(world, pos)) {
            world.setBlockState(pos, state, Block.FORCE_STATE | Block.NOTIFY_ALL);
        }
    }
    
    private BlockState getLeafState(Random random, boolean allowFlowering) {
        var state = SpectrumBlocks.NEPHRITE_BLOSSOM_LEAVES.getDefaultState().with(NephriteBlossomLeavesBlock.DISTANCE, 1);
        if (!allowFlowering) {
            return state;
        }
        if (random.nextBoolean()) {
            return state.with(NephriteBlossomLeavesBlock.AGE, 1);
        }
        if (random.nextBoolean()) {
            return state.with(NephriteBlossomLeavesBlock.AGE, 2);
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
	
	private static boolean isReplaceable(TestableWorld world, BlockPos pos) {
		return world.testBlockState(pos, (state) -> state.isAir() || state.isIn(BlockTags.REPLACEABLE_BY_TREES));
    }

}
