package de.dafuqs.pigment.blocks.private_chest;

import de.dafuqs.pigment.blocks.altar.AltarBlockEntity;
import de.dafuqs.pigment.registries.PigmentBlockEntityTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;


public class PrivateChestBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    protected static final VoxelShape SINGLE_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public PrivateChestBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PrivateChestBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            this.openScreen(world, pos, player);
            return ActionResult.CONSUME;
        }
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, PigmentBlockEntityTypes.PRIVATE_CHEST, PrivateChestBlockEntity::clientTick) : null;
    }

    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof PrivateChestBlockEntity) {
            PrivateChestBlockEntity privateChestBlockEntity = ((PrivateChestBlockEntity) blockEntity);

            if(privateChestBlockEntity.getPlayerUUID() == null) {
                privateChestBlockEntity.setPlayerData(player.getUuid(), player.getName());
            }

            if(!isChestBlocked(world, pos)) {
                // Permissions are handled with vanilla lock()
                player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            }
        }
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AltarBlockEntity) {
                ItemScatterer.spawn(world, pos, (Inventory)blockEntity);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SINGLE_SHAPE;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing().getOpposite();
        return this.getDefaultState().with(FACING, direction);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PrivateChestBlockEntity) {
                ((PrivateChestBlockEntity)blockEntity).setCustomName(itemStack.getName());
            }
        }
    }

    public static boolean isChestBlocked(WorldAccess world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return world.getBlockState(blockPos).isSolidBlock(world, blockPos);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PrivateChestBlockEntity) {
            ((PrivateChestBlockEntity)blockEntity).onScheduledTick();
        }
    }

    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    /*
    The chest emits redstone power of strength...
    7: if the owner has it opened
    15: if it was tried to open by a non-owner in the last 20 ticks
     */
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PrivateChestBlockEntity) {
            if (((PrivateChestBlockEntity) blockEntity).wasRecentlyTriedToOpenByNonOwner()) {
                return 15;
            }
            int lookingPlayers = PrivateChestBlockEntity.getPlayersLookingInChestCount(world, pos);
            if(lookingPlayers > 0) {
                return 7;
            }
        }
        return 0;
    }

    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return direction == Direction.UP ? state.getWeakRedstonePower(world, pos, direction) : 0;
    }

    /*
    Only the chest owner may break it
     */
    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof PrivateChestBlockEntity) {
            PrivateChestBlockEntity privateChestBlockEntity = (PrivateChestBlockEntity) blockEntity;
            if (privateChestBlockEntity.canBreak(player.getUuid())) {
                return super.calcBlockBreakingDelta(state, player, world, pos);
            }
        }
        return -1;
    }

}
