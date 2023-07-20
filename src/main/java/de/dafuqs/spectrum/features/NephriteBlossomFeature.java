package de.dafuqs.spectrum.features;

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

    private static final List<Direction> VALID_DIRS;

    public NephriteBlossomFeature(Codec<NephriteBlossomFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<NephriteBlossomFeatureConfig> context) {
        var world = context.getWorld();
        var origin = context.getOrigin();
        var random = context.getRandom();
        var chunkGen = context.getGenerator();
        var floor = world.getBlockState(origin.down());
        var flowering = context.getConfig().flowering();

        if (!floor.isIn(BlockTags.DIRT))
            return false;

        var stemHeight = Math.round(MathHelper.nextGaussian(random, 2, 1F) + 1);

        if (stemHeight + origin.getY() > chunkGen.getWorldHeight() || !isReplaceable(world, origin, true))
            return false;

        generateStem(world, origin, stemHeight);
        genereateLeaves(world, origin, random, stemHeight, flowering);

        return true;
    }

    private void generateStem(WorldAccess world, BlockPos origin, int stemHeight) {
        var stemPointer = origin.mutableCopy();
        var topStem = false;

        for (int height = 0; height < stemHeight; height++) {

            if (height == 0) {
                this.setBlockState(world, stemPointer, SpectrumBlocks.NEPHRITE_BLOSSOM_STEM.getDefaultState());
                topStem = true;
            }
            else if (isReplaceable(world, stemPointer, true)) {
                this.setBlockState(world, stemPointer, NephriteBlossomStemBlock.getStemVariant(topStem));
                topStem = !topStem;
            }
            stemPointer.move(0, 1, 0);
        }
    }

    private void genereateLeaves(WorldAccess world, BlockPos origin, Random random, int stemHeight, boolean flowering) {

        var leafHeight = Math.round(MathHelper.nextGaussian(random, 2.5F, 0.9F) + 1.85F);
        var leafPointer = origin.mutableCopy().move(0, stemHeight, 0);
        var leafDirection = VALID_DIRS.get(random.nextInt(4));

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
    
    private static void setBlockStateWithoutUpdatingNeighbors(ModifiableWorld world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state, 19);
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

    private static boolean isReplaceable(WorldAccess world, BlockPos pos, boolean replacePlants) {
        return world.testBlockState(pos, (state) -> {
            Material material = state.getMaterial();
            return state.getMaterial().isReplaceable() || replacePlants && material == Material.PLANT;
        });
    }

    static {
        VALID_DIRS = new ArrayList<>();
        VALID_DIRS.add(Direction.NORTH);
        VALID_DIRS.add(Direction.EAST);
        VALID_DIRS.add(Direction.SOUTH);
        VALID_DIRS.add(Direction.WEST);
    }
}
