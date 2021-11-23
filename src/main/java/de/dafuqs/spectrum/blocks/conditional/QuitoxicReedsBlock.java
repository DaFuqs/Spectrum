package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class QuitoxicReedsBlock extends SugarCaneBlock implements Cloakable, Waterloggable {

	public static final IntProperty AGE = Properties.AGE_15;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final BooleanProperty LIQUID_CRYSTAL_LOGGED = SpectrumCommon.LIQUID_CRYSTAL_LOGGED;

	public QuitoxicReedsBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false).with(LIQUID_CRYSTAL_LOGGED, false).with(AGE, 0));
		registerCloak();
	}

	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "build_basic_pedestal_structure");
	}

	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for(int i = 0; i < 16; i++){
			hashtable.put(this.getDefaultState().with(WATERLOGGED, false).with(AGE, i), Blocks.AIR.getDefaultState());
			hashtable.put(this.getDefaultState().with(WATERLOGGED, true).with(AGE, i), Blocks.WATER.getDefaultState());
		}
		return hashtable;
	}

	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.SUGAR_CANE.asItem());
	}


	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return getCloakedDroppedStacks(state, builder);
	}

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		boolean water = fluidState.getFluid() == Fluids.WATER;
		boolean crystal = fluidState.getFluid() == SpectrumFluids.STILL_LIQUID_CRYSTAL;
		return super.getPlacementState(ctx).with(WATERLOGGED, water).with(LIQUID_CRYSTAL_LOGGED, crystal);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canPlaceAt(world, pos)) {
			if(state.get(WATERLOGGED)) {
				return Blocks.WATER.getDefaultState();
			} else if(state.get(LIQUID_CRYSTAL_LOGGED)) {
				return SpectrumBlocks.LIQUID_CRYSTAL.getDefaultState();
			} else {
				return Blocks.AIR.getDefaultState();
			}
		} else {
			if (state.get(WATERLOGGED)) {
				world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			} else if (state.get(LIQUID_CRYSTAL_LOGGED)) {
				world.getFluidTickScheduler().schedule(pos, SpectrumFluids.STILL_LIQUID_CRYSTAL, SpectrumFluids.STILL_LIQUID_CRYSTAL.getTickRate(world));
			}
			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}

	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : state.get(LIQUID_CRYSTAL_LOGGED) ? SpectrumFluids.STILL_LIQUID_CRYSTAL.getStill(false) : super.getFluidState(state);
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, WATERLOGGED, LIQUID_CRYSTAL_LOGGED);
	}

	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.isAir(pos.up()) || (world.isWater(pos.up()) && world.isAir(pos.up(2)))) {

			int i;
			for(i = 1; world.getBlockState(pos.down(i)).isOf(this); ++i) {
			}
			
			boolean bottomLiquidCrystalLogged = world.getBlockState(pos.down(i-1)).get(LIQUID_CRYSTAL_LOGGED);

			// grows taller on liquid crystal
			if (i < 5 || (bottomLiquidCrystalLogged && i < 7)) {
				int j = state.get(AGE);
				if (j == 15) {
					// consume 1 clay block close to the reed when growing.
					// if the quitoxic reeds are growing in liquid crystal:
					// consume 1/4 of clay
					if(!bottomLiquidCrystalLogged || random.nextInt(4) == 0){
						// search for clay. 1 clay => 1 quitoxic reed
						Optional<BlockPos> clayPos = searchClayPos(world, pos.down(i), Blocks.CLAY.getDefaultState(), random);
						if (clayPos.isEmpty() || world.getBlockState(clayPos.get().up()).getBlock() instanceof QuitoxicReedsBlock) {
							return;
						}
						world.setBlockState(clayPos.get(), Blocks.DIRT.getDefaultState(), 3);
						world.playSound(null, clayPos.get(), SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
					
					world.setBlockState(pos.up(), getStateForPos(world, pos.up()));
					world.setBlockState(pos, state.with(AGE, 0), 4);
				} else {
					// grow twice as fast, if liquid crystal logged
					if(bottomLiquidCrystalLogged) {
						world.setBlockState(pos, state.with(AGE, Math.min(15, j + 2)), 4);
					} else {
						world.setBlockState(pos, state.with(AGE, j + 1), 4);
					}
				}
			}
		}
	}

	private Optional<BlockPos> searchClayPos(World world, @NotNull BlockPos searchPos, BlockState searchBlockState, Random random) {
		List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
		Collections.shuffle(directions, random);

		int i = 0;
		int range = 8;
		BlockPos currentPos = new BlockPos(searchPos.getX(), searchPos.getY(), searchPos.getZ());
		while(i < 6) {
			if(range < 8 && world.getBlockState(currentPos.offset(directions.get(i))).equals(searchBlockState)) {
				range++;
				currentPos = currentPos.offset(directions.get(i));
			} else {
				i++;
				range = 0;
			}
		}

		if(currentPos.equals(searchPos)) {
			return Optional.empty();
		} else {
			return Optional.of(currentPos);
		}
	}
	
	public BlockState getStateForPos(World world, BlockPos blockPos) {
		FluidState fluidState = world.getFluidState(blockPos);
		if(fluidState.equals(Fluids.WATER)) {
			return getDefaultState().with(WATERLOGGED, true);
		} else if(fluidState.equals(SpectrumFluids.STILL_LIQUID_CRYSTAL)) {
			return getDefaultState().with(LIQUID_CRYSTAL_LOGGED, true);
		}
		return getDefaultState();
	}

	@Deprecated
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if(this.isVisibleTo(context)) {
			return SHAPE;
		}
		return EMPTY_SHAPE;
	}

	/**
	 * Can be placed in up to 2 blocks deep water / liquid crystal
	 * growing on clay only
	 */
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState bottomBlockState = world.getBlockState(pos.down());
		if (bottomBlockState.isOf(this)) {
			return true;
		} else {
			BlockState topBlockState = world.getBlockState(pos.up());
			BlockState topBlockState2 = world.getBlockState(pos.up(2));
			if (bottomBlockState.isIn(SpectrumBlockTags.QUITOXIC_REEDS_PLANTABLE) && (topBlockState.isAir() || topBlockState2.isAir() || topBlockState.isOf(this) || topBlockState2.isOf(this))) {
				FluidState fluidState = world.getFluidState(pos);
				return fluidState.getLevel() == 8 && (fluidState.isIn(FluidTags.WATER) || fluidState.isIn(SpectrumFluidTags.LIQUID_CRYSTAL));
			}
			return false;
		}
	}

}
