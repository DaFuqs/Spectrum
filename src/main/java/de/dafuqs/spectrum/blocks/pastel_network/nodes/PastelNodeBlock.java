package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PastelNodeBlock extends FacingBlock implements BlockEntityProvider {

    public static final DirectionProperty FACING = Properties.FACING;
    public static final Map<Direction, VoxelShape> SHAPES = new HashMap<>() {{
        put(Direction.UP, Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D));
        put(Direction.DOWN, Block.createCuboidShape(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D));
        put(Direction.NORTH, Block.createCuboidShape(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D));
        put(Direction.SOUTH, Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D));
        put(Direction.EAST, Block.createCuboidShape(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D));
        put(Direction.WEST, Block.createCuboidShape(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D));
    }};

    protected final PastelNodeType pastelNodeType;

    public PastelNodeBlock(Settings settings, PastelNodeType pastelNodeType) {
        super(settings);
        this.pastelNodeType = pastelNodeType;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction targetDirection = state.get(FACING).getOpposite();
        return world.getBlockState(pos.offset(targetDirection)).getMaterial().isSolid();
    }


    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!newState.isOf(state.getBlock())) {
            PastelNodeBlockEntity blockEntity = getBlockEntity(world, pos);
            if (blockEntity != null) {
                blockEntity.onBroken();
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction.getOpposite()));
        return blockState.isOf(this) && blockState.get(FACING) == direction ? this.getDefaultState().with(FACING, direction.getOpposite()) : this.getDefaultState().with(FACING, direction);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        tooltip.add(this.pastelNodeType.getTooltip().formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("block.spectrum.pastel_network_nodes.tooltip.placing").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("block.spectrum.pastel_network_nodes.tooltip.range").formatted(Formatting.GRAY));
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        PastelNodeBlockEntity blockEntity = getBlockEntity(world, pos);
        if (player.getStackInHand(hand).isOf(SpectrumItems.PAINTBRUSH)) {
            return sendDebugMessage(world, player, blockEntity);
        } else if (this.pastelNodeType.usesFilters()) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                player.openHandledScreen(blockEntity);
                return ActionResult.CONSUME;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @NotNull
    private static ActionResult sendDebugMessage(World world, PlayerEntity player, PastelNodeBlockEntity blockEntity) {
        if (world.isClient) {
            if (blockEntity != null) {
                PastelNetwork network = blockEntity.network;
                player.sendMessage(Text.translatable("block.spectrum.pastel_network_nodes.connection_debug"));
                if (network == null) {
                    player.sendMessage(Text.literal("C: No connected network :("));
                } else {
                    player.sendMessage(Text.literal("C: " + network.getUUID().toString()));
                    player.sendMessage(Text.literal("C: " + network.getNodeDebugText()));
                }
            }
            return ActionResult.SUCCESS;
        } else {
            if (blockEntity != null) {
                PastelNetwork network = blockEntity.network;
                if (network == null) {
                    player.sendMessage(Text.literal("S: No connected network :("));
                } else {
                    player.sendMessage(Text.literal("S: " + network.getUUID().toString()));
                    player.sendMessage(Text.literal("S: " + network.getNodeDebugText()));
                }
            }
            return ActionResult.CONSUME;
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    public @Nullable PastelNodeBlockEntity getBlockEntity(WorldAccess world, BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof PastelNodeBlockEntity pastelNodeBlockEntity) {
            return pastelNodeBlockEntity;
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PastelNodeBlockEntity(pos, state);
    }

}