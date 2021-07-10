package de.dafuqs.pigment.blocks.redstone;

import de.dafuqs.pigment.blocks.altar.AltarBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class RedstoneGravityBlock extends FallingBlock {

    public static final BooleanProperty UNSTABLE = BooleanProperty.of("unstable");

    public RedstoneGravityBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(UNSTABLE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(UNSTABLE);
    }

    /**
     * Only trigger fall if redstone applied or unstable
     * if restone: set neighboring block to unstable
     */
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(UNSTABLE) || world.getReceivedRedstonePower(pos) > 0) {
            for(Direction dir : Direction.values()) {
                BlockPos offsetPos = pos.offset(dir);
                if (world.getBlockState(offsetPos).isOf(this)) {
                    world.setBlockState(offsetPos, world.getBlockState(offsetPos).with(UNSTABLE, true));
                    world.getBlockTickScheduler().schedule(offsetPos, this, 1);
                }
            }
            super.scheduledTick(state, world, pos, random);
        }
    }

}
