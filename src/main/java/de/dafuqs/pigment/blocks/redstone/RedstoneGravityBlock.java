package de.dafuqs.pigment.blocks.redstone;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class RedstoneGravityBlock extends FallingBlock {

    public RedstoneGravityBlock(Settings settings) {
        super(settings);
    }

    /**
     * Only trigger fall if redstone applied
     */
    // TODO: pass on fall trigger to neighboring blocks
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(world.getReceivedRedstonePower(pos) > 0) {
            super.scheduledTick(state, world, pos, random);
        }
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            for(Direction dir : Direction.values()) {
                if (world.getBlockState(pos.offset(dir)).isOf(this)) {
                    world.getBlockTickScheduler().schedule(pos.offset(dir), this, 1);
                }
            }
        }
        return state;
    }

}
