package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

import java.util.*;

public class PyriteRipperBlock extends SpectrumFacingBlock {
	
	public static final BooleanProperty MIRRORED = BooleanProperty.of("mirrored");
	
	public static final Map<Direction, VoxelShape> SHAPES = new HashMap<>() {{
		put(Direction.UP, Block.createCuboidShape(0.0D, 0.0D, 5.0D, 16.0D, 6.0D, 11.0D));
		put(Direction.DOWN, Block.createCuboidShape(0.0D, 10.0D, 5.0D, 16.0D, 16.0D, 11.0D));
		put(Direction.NORTH, Block.createCuboidShape(0.0D, 5.0D, 10.0D, 16.0D, 11.0D, 16.0D));
		put(Direction.SOUTH, Block.createCuboidShape(0.0D, 5.0D, 0.0D, 16.0D, 11.0D, 6.0D));
		put(Direction.EAST, Block.createCuboidShape(0.0D, 5.0D, 0.0D, 6.0D, 11.0D, 16.0));
		put(Direction.WEST, Block.createCuboidShape(10.0D, 5.0D, 0.0D, 16.0D, 11.0D, 16.0));
	}};
	public static final Map<Direction, VoxelShape> SHAPES_MIRRORED = new HashMap<>() {{
		put(Direction.UP, Block.createCuboidShape(5.0D, 0.0D, 0.0D, 11.0D, 6.0D, 16.0));
		put(Direction.DOWN, Block.createCuboidShape(5.0D, 10.0D, 0.0D, 11.0D, 16.0D, 16.0D));
		put(Direction.NORTH, Block.createCuboidShape(5.0D, 0.0D, 10.0D, 11.0D, 16.0D, 16.0D));
		put(Direction.SOUTH, Block.createCuboidShape(5.0D, 0.0D, 0.0D, 11.0D, 16.0D, 6.0D));
		put(Direction.EAST, Block.createCuboidShape(0.0D, 0.0D, 5.0D, 6.0D, 16.0D, 11.0D));
		put(Direction.WEST, Block.createCuboidShape(10.0D, 0.0D, 5.0D, 16.0D, 16.0D, 11.0D));
	}};
	
	public PyriteRipperBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(FACING, Direction.EAST).with(MIRRORED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, MIRRORED);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction targetDirection = state.get(FACING).getOpposite();
		return world.getBlockState(pos.offset(targetDirection)).getMaterial().isSolid();
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getSide();
		BlockState placedOnState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction.getOpposite()));
		if (placedOnState.isOf(this)) {
			return placedOnState;
		}
		
		if (ctx.getPlayerFacing().getAxis().isHorizontal()) {
			return this.getDefaultState().with(FACING, direction).with(MIRRORED, ctx.getPlayerFacing().getOffsetX() != 0);
		}
		
		boolean mirrored = ctx.getPlayerLookDirection().getAxis().isVertical();
		return this.getDefaultState().with(FACING, direction).with(MIRRORED, mirrored);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(MIRRORED) ? SHAPES_MIRRORED.get(state.get(FACING)) : SHAPES.get(state.get(FACING));
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity) {
			if (!world.isClient && (entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ())) {
				double difX = Math.abs(entity.getX() - entity.lastRenderX);
				double difZ = Math.abs(entity.getZ() - entity.lastRenderZ);
				if (difX >= 0.003 || difZ >= 0.003) {
					entity.damage(SpectrumDamageSources.RIPPER, 2.0F);
				}
			}
		}
	}
	
}
