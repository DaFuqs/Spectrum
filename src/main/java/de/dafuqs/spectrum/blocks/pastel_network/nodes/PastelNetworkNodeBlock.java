package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
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

public class PastelNetworkNodeBlock extends FacingBlock implements BlockEntityProvider {

    protected static final Map<Direction, VoxelShape> SHAPES = new HashMap<>() {{
        put(Direction.UP, Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D));
        put(Direction.DOWN, Block.createCuboidShape(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D));
        put(Direction.NORTH, Block.createCuboidShape(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D));
        put(Direction.SOUTH, Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D));
        put(Direction.EAST, Block.createCuboidShape(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D));
        put(Direction.WEST, Block.createCuboidShape(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D));
    }};

    public static final DirectionProperty FACING = Properties.FACING;

    protected static final Text RANGE_TOOLTIP_TEXT = Text.translatable("block.spectrum.pastel_network_nodes.tooltip.range").formatted(Formatting.GRAY);
    protected final PastelNodeType type;

    public PastelNetworkNodeBlock(Settings settings, PastelNodeType type) {
        super(settings);
        this.type = type;
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
        return Block.sideCoversSmallSquare(world, pos.offset(targetDirection), targetDirection);
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
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            PastelNodeBlockEntity blockEntity = getBlockEntity(world, pos);
            if (blockEntity != null) {
                Direction attachedDirection = state.get(FACING).getOpposite();
                blockEntity.onPlaced(world, pos, attachedDirection);
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        PastelNodeBlockEntity blockEntity = getBlockEntity(world, pos);
        if (blockEntity != null) {
            blockEntity.onBreak();
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        tooltip.add(this.type.getTooltip());
        tooltip.add(RANGE_TOOLTIP_TEXT);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PastelNodeBlockEntity nodeBlockEntity) {
                PastelNetwork network = nodeBlockEntity.network;
                if (network == null) {
                    player.sendMessage(Text.literal("No connected network :("));
                } else {
                    player.sendMessage(Text.literal("Network: " + network.getName() + "; Count: " + network.getAllNodes().size()));
                }
            }
            return ActionResult.CONSUME;
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    public @Nullable PastelNodeBlockEntity getBlockEntity(World world, BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof PastelNodeBlockEntity pastelNodeBlockEntity) {
            return pastelNodeBlockEntity;
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        switch (this.type) {
            case PULLER -> {
                return new PastelPullerNodeBlockEntity(pos, state);
            }
            case PUSHER -> {
                return new PastelPusherNodeBlockEntity(pos, state);
            }
            case STORAGE -> {
                return new PastelStorageNodeBlockEntity(pos, state);
            }
            case PROVIDER -> {
                return new PastelProviderNodeBlockEntity(pos, state);
            }
            default -> {
                return new PastelConnectionNodeBlockEntity(pos, state);
            }
        }
    }

}