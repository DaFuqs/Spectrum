package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RedstoneTimerBlock extends AbstractRedstoneGateBlock {

    public RedstoneTimerBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false));
    }

    // unused vanilla override
    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 2;
    }

    protected int getUpdateDelayInternal(@NotNull BlockState state, World world, BlockPos pos) {
        if(state.get(POWERED)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return blockEntity instanceof RedstoneTimerBlockEntity ? ((RedstoneTimerBlockEntity)blockEntity).getInactiveDuration() : 0;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return blockEntity instanceof RedstoneTimerBlockEntity ? ((RedstoneTimerBlockEntity)blockEntity).getActiveDuration() : 0;
        }

    }

    public ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof RedstoneTimerBlockEntity redstoneTimerBlockEntity) {
                redstoneTimerBlockEntity.stepTiming((ServerPlayerEntity) player);
            }
            return ActionResult.CONSUME;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!this.isLocked(world, pos, state)) {
            boolean bl = state.get(POWERED);
            boolean bl2 = this.hasPower(world, pos, state);
            if (bl && !bl2) {
                world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
            } else if (!bl) {
                world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_LISTENERS);
                if (!bl2) {
                    world.getBlockTickScheduler().schedule(pos, this, this.getUpdateDelayInternal(state, world, pos), TickPriority.VERY_HIGH);
                }
            }
        }
    }

    protected void updatePowered(World world, BlockPos pos, @NotNull BlockState state) {
        boolean bl = state.get(POWERED);
        boolean bl2 = this.hasPower(world, pos, state);
        if (bl != bl2 && !world.getBlockTickScheduler().isTicking(pos, this)) {
            TickPriority tickPriority = TickPriority.HIGH;
            if (this.isTargetNotAligned(world, pos, state)) {
                tickPriority = TickPriority.EXTREMELY_HIGH;
            } else if (bl) {
                tickPriority = TickPriority.VERY_HIGH;
            }

            world.getBlockTickScheduler().schedule(pos, this, this.getUpdateDelayInternal(state), tickPriority);
        }
    }

}
