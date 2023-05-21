package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class QuitoxicReedsBlock extends Block implements RevelationAware, FluidLogging.SpectrumFluidLoggable {
	
	public static final EnumProperty<FluidLogging.State> LOGGED = FluidLogging.ANY_INCLUDING_NONE;
	public static final IntProperty AGE = Properties.AGE_7;
	public static final BooleanProperty ALWAYS_DROP = BooleanProperty.of("always_drop"); // I have no idea why this works anymore and at this point I am too afraid to ask
	
	public static final int MAX_GROWTH_HEIGHT_WATER = 5;
	public static final int MAX_GROWTH_HEIGHT_CRYSTAL = 7;
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	public QuitoxicReedsBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(LOGGED, FluidLogging.State.NOT_LOGGED).with(ALWAYS_DROP, false).with(AGE, 0));
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return SpectrumCommon.locate("milestones/reveal_quitoxic_reeds");
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (int i = 0; i <= Properties.AGE_7_MAX; i++) {
			hashtable.put(this.getDefaultState().with(LOGGED, FluidLogging.State.NOT_LOGGED).with(AGE, i), Blocks.AIR.getDefaultState());
			hashtable.put(this.getDefaultState().with(LOGGED, FluidLogging.State.WATER).with(AGE, i), Blocks.WATER.getDefaultState());
			hashtable.put(this.getDefaultState().with(LOGGED, FluidLogging.State.LIQUID_CRYSTAL).with(AGE, i), SpectrumBlocks.LIQUID_CRYSTAL.getDefaultState());
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.SUGAR_CANE.asItem());
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		if (fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8) {
			return super.getPlacementState(ctx).with(LOGGED, FluidLogging.State.WATER);
		} else if (fluidState.getFluid() == SpectrumFluids.LIQUID_CRYSTAL) {
			return super.getPlacementState(ctx).with(LOGGED, FluidLogging.State.LIQUID_CRYSTAL);
		} else {
			return super.getPlacementState(ctx).with(LOGGED, FluidLogging.State.NOT_LOGGED);
		}
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		// since the quitoxic reeds are stacked and break from bottom to top
		// bot the player that broke the other blocks is not propagated we
		// have to apply a workaround here by counting the reeds above this
		// and dropping that many times loot to account for it
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
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		FluidLogging.State fluidLog = state.get(LOGGED);
		if (fluidLog == FluidLogging.State.WATER) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		} else if (fluidLog == FluidLogging.State.LIQUID_CRYSTAL) {
			world.scheduleFluidTick(pos, SpectrumFluids.LIQUID_CRYSTAL, SpectrumFluids.LIQUID_CRYSTAL.getTickRate(world));
		}
		
		if (!state.canPlaceAt(world, pos)) {
			world.scheduleBlockTick(pos, this, 1);
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(LOGGED).getFluidState();
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, LOGGED, ALWAYS_DROP);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.isAir(pos.up()) || (world.isWater(pos.up()) && world.isAir(pos.up(2)))) {
			
			int i;
			for (i = 1; world.getBlockState(pos.down(i)).isOf(this); ++i) {
			}
			
			boolean bottomLiquidCrystalLogged = world.getBlockState(pos.down(i - 1)).get(LOGGED) == FluidLogging.State.LIQUID_CRYSTAL;
			
			// grows taller on liquid crystal
			if (i < MAX_GROWTH_HEIGHT_WATER || (bottomLiquidCrystalLogged && i < MAX_GROWTH_HEIGHT_CRYSTAL)) {
				int j = state.get(AGE);
				if (j == 7) {
					// consume 1 block close to the reed when growing.
					// if the quitoxic reeds are growing in liquid crystal: 1/4 chance to consume
					if (!bottomLiquidCrystalLogged || random.nextInt(4) == 0) {
						// search for block it could be planted on. 1 block => 1 quitoxic reed
						Optional<BlockPos> plantablePos = searchPlantablePos(world, pos.down(i), SpectrumBlockTags.QUITOXIC_REEDS_PLANTABLE, random);
						if (plantablePos.isEmpty() || world.getBlockState(plantablePos.get().up()).getBlock() instanceof QuitoxicReedsBlock) {
							return;
						}
						world.setBlockState(plantablePos.get(), Blocks.DIRT.getDefaultState(), 3);
						world.playSound(null, plantablePos.get(), SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
	
	private Optional<BlockPos> searchPlantablePos(World world, @NotNull BlockPos searchPos, TagKey<Block> searchBlockState, Random random) {
		List<Direction> directions = Util.copyShuffled(Direction.values(), random);
		
		int i = 0;
		int range = 8;
		BlockPos currentPos = new BlockPos(searchPos.getX(), searchPos.getY(), searchPos.getZ());
		while (i < 6) {
			if (range < 8 && world.getBlockState(currentPos.offset(directions.get(i))).isIn(searchBlockState)) {
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
		if (fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8) {
			return getDefaultState().with(LOGGED, FluidLogging.State.WATER);
		} else if (fluidState.getFluid().equals(SpectrumFluids.LIQUID_CRYSTAL)) {
			return getDefaultState().with(LOGGED, FluidLogging.State.LIQUID_CRYSTAL);
		}
		return getDefaultState();
	}
	
	@Deprecated
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (this.isVisibleTo(context)) {
			return SHAPE;
		}
		return VoxelShapes.empty();
	}
	
	/**
	 * Can be placed in up to 2 blocks deep water / liquid crystal
	 * growing on SpectrumBlockTags.QUITOXIC_REEDS_PLANTABLE only
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
	
}
