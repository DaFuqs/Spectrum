package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class PylonBlock extends Block implements SimpleWaterloggedBlock {
	
	public static final MapCodec<PylonBlock> CODEC = simpleCodec(PylonBlock::new);
	
	public static final EnumProperty<Section> SECTION = EnumProperty.create("section", Section.class);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final BooleanProperty PEDESTAL = BooleanProperty.create("pedestal");
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public static final Map<Direction.Axis, VoxelShape> PYLON_SHAPES;
	public static final Map<Direction, VoxelShape> PEDESTAL_SHAPES;
	
	public PylonBlock(Properties settings) {
		super(settings);
		
		registerDefaultState(getStateDefinition().any()
				.setValue(WATERLOGGED, false)
				.setValue(SECTION, Section.FOOT)
				.setValue(FACING, Direction.UP)
				.setValue(PEDESTAL, false));
	}
	
	@Override
	public MapCodec<? extends PylonBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
		var world = ctx.getLevel();
		var pos = ctx.getClickedPos();
		var state = defaultBlockState();
		var player = ctx.getPlayer();
		boolean shifting = false;
		
		if (player != null)
			shifting = player.isShiftKeyDown();
		
		Section placedSection = shifting ? Section.BODY : Section.HEAD;
		
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		state = state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
		
		var placementDirection = ctx.getClickedFace().getOpposite();
		state = state.setValue(FACING, placementDirection.getOpposite());
		
		var floorPos = pos.relative(placementDirection);
		var floorState = world.getBlockState(floorPos);
		
		updateFloor:
		{
			if (floorState.getBlock() instanceof PylonBlock) {
				var floorFacing = floorState.getValue(FACING);
				
				if (floorFacing.getAxis() != placementDirection.getAxis())
					break updateFloor;
				
				if (floorFacing == placementDirection.getOpposite()) {
					var floorSection = floorState.getValue(SECTION);
					var newFloor = updatePylonBelow(world.getBlockState(floorPos.relative(placementDirection)), floorState, floorSection);
					world.setBlockAndUpdate(floorPos, newFloor);
				}
				
				state = state.setValue(SECTION, placedSection);
				return state;
			}
		}
		
		if (floorState.isFaceSturdy(world, floorPos, placementDirection.getOpposite(), SupportType.CENTER))
			state = state.setValue(PEDESTAL, !shifting).setValue(SECTION, placedSection);
		
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
		boolean base = !(floor.getBlock() instanceof PylonBlock);
		var checkedSection = base ? oldSection : floor.getValue(SECTION);
		
		var newSection = switch (checkedSection) {
			case HEAD -> Section.FOOT;
			case BODY -> Section.BODY;
			default -> base ? oldSection : shiftSection(checkedSection);
		};
		
		return pylon.setValue(SECTION, newSection);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, SECTION, PEDESTAL, WATERLOGGED);
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		var facing = state.getValue(FACING);
		var shape = PYLON_SHAPES.get(facing.getAxis());
		
		if (state.getValue(PEDESTAL))
			shape = Shapes.or(shape, PEDESTAL_SHAPES.get(facing));
		
		return shape;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}
	
	public enum Section implements StringRepresentable {
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
		public String getSerializedName() {
			return this.name;
		}
	}
	
	static {
		var pylonBuilder = ImmutableMap.<Direction.Axis, VoxelShape>builder();
		pylonBuilder.put(Direction.Axis.X, Block.box(0, 3, 3, 16, 13, 13));
		pylonBuilder.put(Direction.Axis.Y, Block.box(3, 0, 3, 13, 16, 13));
		pylonBuilder.put(Direction.Axis.Z, Block.box(3, 3, 0, 13, 13, 16));
		PYLON_SHAPES = pylonBuilder.build();
		
		var pedestalBuilder = ImmutableMap.<Direction, VoxelShape>builder();
		pedestalBuilder.put(Direction.NORTH, Block.box(0, 0, 14, 16, 16, 16));
		pedestalBuilder.put(Direction.SOUTH, Block.box(0, 0, 0, 16, 16, 2));
		pedestalBuilder.put(Direction.WEST, Block.box(14, 0, 0, 16, 16, 16));
		pedestalBuilder.put(Direction.EAST, Block.box(0, 0, 0, 2, 16, 16));
		pedestalBuilder.put(Direction.UP, Block.box(0, 0, 0, 16, 2, 16));
		pedestalBuilder.put(Direction.DOWN, Block.box(0, 14, 0, 16, 16, 16));
		PEDESTAL_SHAPES = pedestalBuilder.build();
	}
}
