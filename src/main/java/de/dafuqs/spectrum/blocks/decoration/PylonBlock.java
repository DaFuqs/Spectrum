package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.*;
import de.dafuqs.spectrum.blocks.*;
import net.minecraft.block.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PylonBlock extends Block implements Waterloggable {

    public static final EnumProperty<Section> SECTION = EnumProperty.of("section", Section.class);
    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final BooleanProperty PEDESTAL = BooleanProperty.of("pedestal");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public static final Map<Direction.Axis, VoxelShape> PYLON_SHAPES;
    public static final Map<Direction, VoxelShape> PEDESTAL_SHAPES;
    
    public PylonBlock(Settings settings) {
        super(settings);

        setDefaultState(getStateManager().getDefaultState()
                .with(WATERLOGGED, false)
                .with(SECTION, Section.FOOT)
                .with(FACING, Direction.UP)
                .with(PEDESTAL, false));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var world = ctx.getWorld();
        var pos = ctx.getBlockPos();
        var state = getDefaultState();
        var player = ctx.getPlayer();
        boolean shifting = false;

        if (player != null)
            shifting = player.isSneaking();

        Section placedSection = shifting ? Section.BODY : Section.HEAD;

        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        state = state.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        var placementDirection = ctx.getSide().getOpposite();
        state = state.with(FACING, placementDirection.getOpposite());

        var floorPos = pos.offset(placementDirection);
        var floorState = world.getBlockState(floorPos);

        updateFloor: {
            if (floorState.getBlock() instanceof PylonBlock) {
                var floorFacing = floorState.get(FACING);

                if (floorFacing.getAxis() != placementDirection.getAxis())
                    break updateFloor;

               if (floorFacing == placementDirection.getOpposite()) {
                   var floorSection = floorState.get(SECTION);;
                   var newFloor = updatePylonBelow(world.getBlockState(floorPos.offset(placementDirection)), floorState, floorSection);
                   world.setBlockState(floorPos, newFloor);
               }

                state = state.with(SECTION, placedSection);
                return state;
            }
        }

        if (floorState.isSideSolid(world, floorPos, placementDirection.getOpposite(), SideShapeType.CENTER))
            state = state.with(PEDESTAL, !shifting).with(SECTION, placedSection);

        return state;
    }

    public Section shiftSection(Section section) {
        return switch (section) {
            case HEAD, BODY -> Section.HEAD;
            case WAIST -> Section.BODY;
            case FOOT -> Section.WAIST;
        };
    }

    public BlockState updatePylonBelow(BlockState floor, BlockState pylon, Section oldSection) {
        boolean base = !(floor.getBlock() instanceof  PylonBlock);
        var checkedSection = base ? oldSection : floor.get(SECTION);

        var newSection = switch (checkedSection) {
            case HEAD -> Section.FOOT;
            case BODY -> Section.BODY;
            default -> base ? oldSection : shiftSection(checkedSection);
        };

        return pylon.with(SECTION, newSection);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, SECTION, PEDESTAL, WATERLOGGED);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(FACING, mirror.apply(state.get(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        var facing = state.get(FACING);
        var shape = PYLON_SHAPES.get(facing.getAxis());

        if (state.get(PEDESTAL))
            shape = VoxelShapes.union(shape, PEDESTAL_SHAPES.get(facing));

        return shape;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    public enum Section implements StringIdentifiable {
        HEAD("head"),
        BODY("body"),
        WAIST("waist"),
        FOOT("foot");

        private final String name;

        Section(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

    static {
        var pylonBuilder = ImmutableMap.<Direction.Axis, VoxelShape>builder();
        pylonBuilder.put(Direction.Axis.X, Block.createCuboidShape(0, 3, 3, 16, 13, 13));
        pylonBuilder.put(Direction.Axis.Y, Block.createCuboidShape(3, 0, 3, 13, 16, 13));
        pylonBuilder.put(Direction.Axis.Z, Block.createCuboidShape(3, 3, 0, 13, 13, 16));
        PYLON_SHAPES = pylonBuilder.build();

        var pedestalBuilder = ImmutableMap.<Direction, VoxelShape>builder();
        pedestalBuilder.put(Direction.NORTH, Block.createCuboidShape(0, 0, 14, 16, 16, 16));
        pedestalBuilder.put(Direction.SOUTH, Block.createCuboidShape(0, 0, 0, 16, 16, 2));
        pedestalBuilder.put(Direction.WEST, Block.createCuboidShape(14, 0, 0, 16, 16, 16));
        pedestalBuilder.put(Direction.EAST, Block.createCuboidShape(0, 0, 0, 2, 16, 16));
        pedestalBuilder.put(Direction.UP, Block.createCuboidShape(0, 0, 0, 16, 2, 16));
        pedestalBuilder.put(Direction.DOWN, Block.createCuboidShape(0, 14, 0, 16, 16, 16));
        PEDESTAL_SHAPES = pedestalBuilder.build();
    }
}
