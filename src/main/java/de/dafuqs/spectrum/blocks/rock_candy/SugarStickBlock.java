package de.dafuqs.spectrum.blocks.rock_candy;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.InWorldInteractionHelper;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class SugarStickBlock extends Block implements RockCandy {
	
	protected final RockCandyVariant rockCandyVariant;
	
	public static final int SUGAR_SEARCH_RANGE = 2;
	public static final int REQUIRED_SUGAR_PER_STAGE = 4;
	
	public static final IntProperty AGE = Properties.AGE_2;
	public static final BooleanProperty LIQUID_CRYSTAL_LOGGED  = SpectrumCommon.LIQUID_CRYSTAL_LOGGED;
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(5.0D, 3.0D, 5.0D, 11.0D, 13.0D, 11.0D);
	
	public SugarStickBlock(Settings settings, RockCandyVariant rockCandyVariant) {
		super(settings);
		this.rockCandyVariant = rockCandyVariant;
		this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0).with(LIQUID_CRYSTAL_LOGGED, false));
	}
	
	@Override
	public RockCandyVariant getVariant() {
		return this.rockCandyVariant;
	}
	
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		if (fluidState.getFluid() == SpectrumFluids.LIQUID_CRYSTAL) {
			return super.getPlacementState(ctx).with(LIQUID_CRYSTAL_LOGGED, true);
		} else {
			return super.getPlacementState(ctx);
		}
	}
	
	public FluidState getFluidState(BlockState state) {
		return state.get(LIQUID_CRYSTAL_LOGGED) ? SpectrumFluids.LIQUID_CRYSTAL.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, LIQUID_CRYSTAL_LOGGED);
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return state.get(LIQUID_CRYSTAL_LOGGED) && state.get(AGE) < Properties.AGE_2_MAX;
	}
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if(state.get(LIQUID_CRYSTAL_LOGGED)) {
			int age = state.get(AGE);
			if(age < Properties.AGE_2_MAX) {
				boolean foundSugar = InWorldInteractionHelper.findAndDecreaseClosestItemEntityOfItem(world, Vec3d.ofCenter(pos), Items.SUGAR, SUGAR_SEARCH_RANGE, REQUIRED_SUGAR_PER_STAGE);
				if(foundSugar) {
					age++;
					world.setBlockState(pos, state.with(AGE, age));
				}
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText("spectrum.block.rock_candy." + this.rockCandyVariant + ".tooltip"));
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
