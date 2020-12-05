package de.dafuqs.spectrum.blocks.buddingblocks;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

public abstract class BuddingSpectrumBlock extends BuddingAmethystBlock {

    private static final Direction[] DIRECTIONS = Direction.values();

    public BuddingSpectrumBlock(Settings settings) {
        super(settings);
    }

    protected abstract Block getSmall();
    protected abstract Block getMedium();
    protected abstract Block getLarge();
    protected abstract Block getCluster();

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(5) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            Block block = null;
            if (BuddingAmethystBlock.method_31626(blockState)) {
                block = getSmall();
            } else if (blockState.isOf(getSmall()) && blockState.get(AmethystClusterBlock.FACING) == direction) {
                block = getMedium();
            } else if (blockState.isOf(getMedium()) && blockState.get(AmethystClusterBlock.FACING) == direction) {
                block = getLarge();
            } else if (blockState.isOf(getLarge()) && blockState.get(AmethystClusterBlock.FACING) == direction) {
                block = getCluster();
            }

            if (block != null) {
                BlockState blockState2 = (block.getDefaultState().with(AmethystClusterBlock.FACING, direction)).with(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
                world.setBlockState(blockPos, blockState2);
            }

        }
    }

}
