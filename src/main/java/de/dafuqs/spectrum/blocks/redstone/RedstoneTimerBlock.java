package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class RedstoneTimerBlock extends AbstractRedstoneGateBlock implements BlockEntityProvider {

    public RedstoneTimerBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneTimerBlockEntity(pos, state);
    }

    // unused vanilla override
    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 2;
    }

    protected int getUpdateDelayInternal(boolean powered, @NotNull World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(powered) {
            return blockEntity instanceof RedstoneTimerBlockEntity ? ((RedstoneTimerBlockEntity)blockEntity).getActiveDuration() : 0;
        } else {
            return blockEntity instanceof RedstoneTimerBlockEntity ? ((RedstoneTimerBlockEntity)blockEntity).getInactiveDuration() : 0;
        }
    }

    public ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, @NotNull PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            if(!world.isClient) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof RedstoneTimerBlockEntity redstoneTimerBlockEntity) {
                    redstoneTimerBlockEntity.stepTiming((ServerPlayerEntity) player);
                }
            }
            return ActionResult.success(world.isClient);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean powered = state.get(POWERED);
        if (powered) {
            world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
        } else {
            world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_LISTENERS);
        }
        world.getBlockTickScheduler().schedule(pos, this, this.getUpdateDelayInternal(powered, world, pos), TickPriority.VERY_HIGH);
    }

    @Override
    public void updatePowered(World world, BlockPos pos, @NotNull BlockState state) {
        boolean powered = state.get(POWERED);
        TickPriority tickPriority = TickPriority.HIGH;
        if (this.isTargetNotAligned(world, pos, state)) {
            tickPriority = TickPriority.EXTREMELY_HIGH;
        } else if (powered) {
            tickPriority = TickPriority.VERY_HIGH;
        }

        world.getBlockTickScheduler().schedule(pos, this, this.getUpdateDelayInternal(powered, world, pos), tickPriority);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if(!world.isClient) {
            updatePowered(world, pos, state);
        }
    }

}
