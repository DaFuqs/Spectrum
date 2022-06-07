package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.WaterOrLiquidCrystalLogged;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
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

public class QuitoxicReedsBlock extends Block implements Cloakable, WaterOrLiquidCrystalLogged {
	
	public static final IntProperty AGE = Properties.AGE_7;
	public static final int MAX_GROWTH_HEIGHT_WATER = 5;
	public static final int MAX_GROWTH_HEIGHT_CRYSTAL = 7;
	public static final IntProperty FLUIDLOGGED = WaterOrLiquidCrystalLogged.FLUIDLOGGED;
	public static final BooleanProperty ALWAYS_DROP = BooleanProperty.of("always_drop");
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	public QuitoxicReedsBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FLUIDLOGGED, 0).with(ALWAYS_DROP, false).with(AGE, 0));
		registerCloak();
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_quitoxic_reeds");
	}
	
	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (int i = 0; i < 8; i++) {
			hashtable.put(this.getDefaultState().with(FLUIDLOGGED, 0).with(AGE, i), Blocks.AIR.getDefaultState());
			hashtable.put(this.getDefaultState().with(FLUIDLOGGED, 1).with(AGE, i), Blocks.WATER.getDefaultState());
			hashtable.put(this.getDefaultState().with(FLUIDLOGGED, 2).with(AGE, i), SpectrumBlocks.LIQUID_CRYSTAL.getDefaultState());
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.SUGAR_CANE.asItem());
	}
	
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.afterBreak(world, player, pos, state, blockEntity, stack);
	}
	
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}
	
	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		if (state.get(ALWAYS_DROP)) {
			return super.getDroppedStacks(state, builder);
		} else {
			return getCloakedDroppedStacks(state, builder);
		}
	}
	
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		if (fluidState.getFluid() == Fluids.WATER) {
			return super.getPlacementState(ctx).with(FLUIDLOGGED, 1);
		} else if (fluidState.getFluid() == SpectrumFluids.LIQUID_CRYSTAL) {
			return super.getPlacementState(ctx).with(FLUIDLOGGED, 2);
		} else {
			return super.getPlacementState(ctx).with(FLUIDLOGGED, 0);
		}
	}
	
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		// since the quitoxic reeds are stacked and break from bottom to top
		// bot the player that broke the other blocks is not propagated we
		// have to apply a workaround here by counting the reeds above this
		// and dropping that much times loot to account for it
		for (int i = 1; i < MAX_GROWTH_HEIGHT_CRYSTAL; i++) {
			BlockPos offsetPos = pos.add(0, i, 0);
			if (world.getBlockState(offsetPos).isOf(this)) {
				world.setBlockState(offsetPos, world.getBlockState(offsetPos).with(ALWAYS_DROP, true));
			} else {
				break;
			}
		}
		
		super.onBreak(world, pos, state, player);
	}
	
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		int fluidLog = state.get(FLUIDLOGGED);
		if (fluidLog == 1) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		} else if (fluidLog == 2) {
			world.createAndScheduleFluidTick(pos, SpectrumFluids.LIQUID_CRYSTAL, SpectrumFluids.LIQUID_CRYSTAL.getTickRate(world));
		}
		
		if (!state.canPlaceAt(world, pos)) {
			world.createAndScheduleBlockTick(pos, this, 1);
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	public FluidState getFluidState(BlockState state) {
		int fluidLog = state.get(FLUIDLOGGED);
		if (fluidLog == 1) {
			return Fluids.WATER.getStill(false);
		} else if (fluidLog == 2) {
			return SpectrumFluids.LIQUID_CRYSTAL.getStill(false);
		} else {
			return super.getFluidState(state);
		}
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, FLUIDLOGGED, ALWAYS_DROP);
	}
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.isAir(pos.up()) || (world.isWater(pos.up()) && world.isAir(pos.up(2)))) {
			
			int i;
			for (i = 1; world.getBlockState(pos.down(i)).isOf(this); ++i) {
			}
			
			boolean bottomLiquidCrystalLogged = world.getBlockState(pos.down(i - 1)).get(FLUIDLOGGED) == 2;
			
			// grows taller on liquid crystal
			if (i < MAX_GROWTH_HEIGHT_WATER || (bottomLiquidCrystalLogged && i < MAX_GROWTH_HEIGHT_CRYSTAL)) {
				int j = state.get(AGE);
				if (j == 7) {
					// consume 1 clay block close to the reed when growing.
					// if the quitoxic reeds are growing in liquid crystal:
					// consume 1/4 of clay
					if (!bottomLiquidCrystalLogged || random.nextInt(4) == 0) {
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
					if (bottomLiquidCrystalLogged) {
						world.setBlockState(pos, state.with(AGE, Math.min(7, j + 2)), 4);
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
		while (i < 6) {
			if (range < 8 && world.getBlockState(currentPos.offset(directions.get(i))).equals(searchBlockState)) {
				range++;
				currentPos = currentPos.offset(directions.get(i));
			} else {
				i++;
				range = 0;
			}
		}
		
		if (currentPos.equals(searchPos)) {
			return Optional.empty();
		} else {
			return Optional.of(currentPos);
		}
	}
	
	public BlockState getStateForPos(World world, BlockPos blockPos) {
		FluidState fluidState = world.getFluidState(blockPos);
		if (fluidState.getFluid().equals(Fluids.WATER)) {
			return getDefaultState().with(FLUIDLOGGED, 1);
		} else if (fluidState.getFluid().equals(SpectrumFluids.LIQUID_CRYSTAL)) {
			return getDefaultState().with(FLUIDLOGGED, 2);
		}
		return getDefaultState();
	}
	
	@Deprecated
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (this.isVisibleTo(context)) {
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
			if (bottomBlockState.isIn(SpectrumBlockTags.QUITOXIC_REEDS_PLANTABLE) && isValidTopBlock(topBlockState) && isValidTopBlock(topBlockState2)) {
				FluidState fluidState = world.getFluidState(pos);
				return fluidState.getLevel() == 8 && (fluidState.isIn(FluidTags.WATER) || fluidState.isIn(SpectrumFluidTags.LIQUID_CRYSTAL));
			}
			return false;
		}
	}
	
	public boolean isValidTopBlock(BlockState blockState) {
		return blockState.isAir() || blockState.isOf(this) || blockState.isOf(Blocks.WATER) || blockState.isOf(SpectrumBlocks.LIQUID_CRYSTAL);
	}
	
	@Override
	public float getMaxHorizontalModelOffset() {
		return 0.15F;
	}
	
	@Override
	public OffsetType getOffsetType() {
		return OffsetType.XYZ;
	}
}
