package de.dafuqs.spectrum.blocks.pedestal;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PedestalUpgradeBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = Properties.FACING;

    protected static final VoxelShape SHAPE_FLOATING = Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    protected static final VoxelShape SHAPE_UP = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
    protected static final VoxelShape SHAPE_DOWN = Block.createCuboidShape(2.0D, 6.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(2.0D, 2.0D, 6.0D, 14.0D, 14.0D, 16.0D);
    protected static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 10.0D);
    protected static final VoxelShape SHAPE_EAST = Block.createCuboidShape(0.0D, 2.0D, 2.0D, 10.0D, 14.0D, 14.0D);
    protected static final VoxelShape SHAPE_WEST = Block.createCuboidShape(6.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D);

    public PedestalUpgradeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(((this.stateManager.getDefaultState()).with(FACING, Direction.UP)));
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
            case UP -> {
                return SHAPE_UP;
            }
            case DOWN -> {
                return SHAPE_DOWN;
            }
            case NORTH -> {
                return SHAPE_NORTH;
            }
            case EAST -> {
                return SHAPE_EAST;
            }
            case SOUTH -> {
                return SHAPE_SOUTH;
            }
            default -> {
                return SHAPE_WEST;
            }
        }
    }

    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide());
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

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        updateConnectedPedestal(world, pos);
        super.onBlockAdded(state, world, pos, oldState, notify);
    }


    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        updateConnectedPedestal(world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    /**
     * When placed or removed the upgrade block searches for a valid pedestal
     * and triggers it to update its upgrades
     */
    private void updateConnectedPedestal(@NotNull World world, @NotNull BlockPos pos) {
        if(world.getBlockState(pos.add(3, -2, 3)).getBlock() instanceof PedestalBlock) {
            PedestalBlock.updateUpgrades(world, pos.add(3, -2, 3));
        } else if(world.getBlockState(pos.add(3, -2, -3)).getBlock() instanceof PedestalBlock) {
            PedestalBlock.updateUpgrades(world, pos.add(3, -2, -3));
        } else if(world.getBlockState(pos.add(-3, -2, 3)).getBlock() instanceof PedestalBlock) {
            PedestalBlock.updateUpgrades(world, pos.add(-3, -2, 3));
        } else if(world.getBlockState(pos.add(-3, -2, -3)).getBlock() instanceof PedestalBlock) {
            PedestalBlock.updateUpgrades(world, pos.add(-3, -2, -3));
        }
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalUpgradeBlockEntity(pos, state);
    }
}
