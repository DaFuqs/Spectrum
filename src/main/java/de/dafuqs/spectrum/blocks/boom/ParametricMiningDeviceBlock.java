package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.explosion.*;
import net.minecraft.block.*;
import net.minecraft.block.piston.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ParametricMiningDeviceBlock extends PlaceableItemBlock {
	
	public static final DirectionProperty FACING = Properties.FACING;
	
	public static final Map<Direction, VoxelShape> SHAPES = new HashMap<>() {{
		put(Direction.UP, Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D));
		put(Direction.DOWN, Block.createCuboidShape(4.0D, 12.0D, 4.0D, 12.0D, 16.0D, 12.0D));
		put(Direction.NORTH, Block.createCuboidShape(4.0D, 4.0D, 12.0D, 12.0D, 12.0D, 16.0D));
		put(Direction.SOUTH, Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 4.0D));
		put(Direction.EAST, Block.createCuboidShape(0.0D, 4.0D, 4.0D, 4.0D, 12.0D, 12.0D));
		put(Direction.WEST, Block.createCuboidShape(12.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D));
	}};
	
	public ParametricMiningDeviceBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(FacingBlock.FACING, Direction.UP));
	}
	
	// Wall mounting stuffs
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(FACING);
		BlockPos blockPos = pos.offset(direction.getOpposite());
		return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return direction == state.get(FACING).getOpposite() && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getSide());
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(FACING));
	}
	
	// misc
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	// actual logic
	// press to boom
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}
		
		if ((world.getBlockEntity(pos) instanceof PlacedItemBlockEntity blockEntity)) {
			ItemStack stack = blockEntity.getStack();
			PlayerEntity owner = blockEntity.getOwnerIfOnline();
			
			world.removeBlock(pos, false);
			
			ModularExplosionDefinition.explode((ServerWorld) world, pos, state.get(FACING).getOpposite(), owner, stack);
		}
		
		return ActionResult.CONSUME;
	}
	
}
