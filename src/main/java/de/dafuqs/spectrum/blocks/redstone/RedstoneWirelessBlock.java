package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RedstoneWirelessBlock extends AbstractRedstoneGateBlock implements BlockEntityProvider {

    public RedstoneWirelessBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneWirelessBlockEntity(pos, state);
    }

    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 0;
    }

    @Override
    public ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RedstoneWirelessBlockEntity redstoneWirelessBlockEntity) {
                redstoneWirelessBlockEntity.toggleSendingMode();
                updatePowered(world, pos, state);
                return ActionResult.CONSUME;
            } else {
                return super.onUse(state, world, pos, player, hand, hit);
            }
        }
    }

    // Hmmm... my feelings tell me that using channels and sender/receiver as property might be a bit much (16 dye colors) * 2 states plus the already existing states
    // Better move it to the block entity and use a dynamic renderer.
    // Not that performant when rendering but not consuming so much RAM like 16 times the block states.
    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> GameEventListener getGameEventListener(World world, T blockEntity) {
        return blockEntity instanceof RedstoneWirelessBlockEntity ? ((RedstoneWirelessBlockEntity)blockEntity).getEventListener() : null;
    }

    @Override
    public void updatePowered(World world, BlockPos pos, BlockState state) {
        int newSignal = this.getPower(world, pos, state);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof RedstoneWirelessBlockEntity redstoneWirelessBlockEntity) {
            if(redstoneWirelessBlockEntity.isSending()) {
                int lastSignal = redstoneWirelessBlockEntity.getCurrentSignalStrength();
                if(newSignal != lastSignal) {
                    redstoneWirelessBlockEntity.setSignalStrength(newSignal);
                }

                if (newSignal == 0) {
                    world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
                } else {
                    world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }

    /**
     * The block entity caches the output signal for performance
     */
    protected int getOutputLevel(@NotNull BlockView world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof RedstoneWirelessBlockEntity ? ((RedstoneWirelessBlockEntity)blockEntity).getOutputSignal() : 0;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if(!world.isClient) {
            updatePowered(world, pos, state);
        }
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(!world.isClient) {
            return checkType(type, SpectrumBlockEntityRegistry.REDSTONE_WIRELESS, RedstoneWirelessBlockEntity::serverTick);
        }
        return null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

}
