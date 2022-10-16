package de.dafuqs.spectrum.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class RockCandyBlock extends Block {
	
	public static final IntProperty AGE = Properties.AGE_3;
	public static final EnumProperty<RockCandyVariant> ROCK_CANDY_VARIANT  = EnumProperty.of("color", RockCandyVariant.class);
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(5.0D, 3.0D, 5.0D, 11.0D, 13.0D, 11.0D);
	
	enum RockCandyVariant implements StringIdentifiable {
		NONE,
		AMETHYST,
		CITRINE,
		TOPAZ,
		ONYX,
		MOONSTONE;
		
		@Override
		public String asString() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
	
	public RockCandyBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0).with(ROCK_CANDY_VARIANT, RockCandyVariant.NONE));
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, ROCK_CANDY_VARIANT);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = Direction.UP;
		return Block.sideCoversSmallSquare(world, pos.offset(direction), direction.getOpposite());
	}
	
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.UP && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
}
