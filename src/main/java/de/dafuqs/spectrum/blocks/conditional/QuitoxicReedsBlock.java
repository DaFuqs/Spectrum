package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.concurrent.atomic.*;

public class QuitoxicReedsBlock extends Block implements RevelationAware, FluidLogging.SpectrumFluidLoggable {
	
	public static final MapCodec<QuitoxicReedsBlock> CODEC = simpleCodec(QuitoxicReedsBlock::new);
	
	public static final EnumProperty<FluidLogging.State> LOGGED = FluidLogging.AIR_WATER_LIQUID_CRYSTAL_DRAGONROT;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
	
	// 'always drop' has no cloak and therefore drops normally even when broken 'via the world'
	// without player context (the reeds dropping one after one on scheduledTick())
	public static final BooleanProperty ALWAYS_DROP = BooleanProperty.create("always_drop");
	
	public static final int MAX_GROWTH_HEIGHT_WATER = 5;
	public static final int MAX_GROWTH_HEIGHT_CRYSTAL = 7;
	
	public static final int MAX_CONSUMABLE_BLOCK_SEARCH_DISTANCE = 8;
	
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	public QuitoxicReedsBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(LOGGED, FluidLogging.State.NOT_LOGGED).setValue(ALWAYS_DROP, false).setValue(AGE, 0));
		RevelationAware.register(this);
	}
	
	@Override
	public MapCodec<? extends QuitoxicReedsBlock> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_QUITOXIC_REEDS;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (int i = 0; i <= BlockStateProperties.MAX_AGE_7; i++) {
			map.put(this.defaultBlockState().setValue(LOGGED, FluidLogging.State.NOT_LOGGED).setValue(AGE, i), Blocks.AIR.defaultBlockState());
			map.put(this.defaultBlockState().setValue(LOGGED, FluidLogging.State.WATER).setValue(AGE, i), Blocks.WATER.defaultBlockState());
			map.put(this.defaultBlockState().setValue(LOGGED, FluidLogging.State.LIQUID_CRYSTAL).setValue(AGE, i), SpectrumBlocks.LIQUID_CRYSTAL.get().defaultBlockState());
			map.put(this.defaultBlockState().setValue(LOGGED, FluidLogging.State.DRAGONROT).setValue(AGE, i), SpectrumBlocks.DRAGONROT.get().defaultBlockState());
		}
		return map;
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.SUGAR_CANE.asItem());
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return getStateForPos(ctx.getLevel(), ctx.getClickedPos());
	}
	
	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		// since the quitoxic reeds are stacked and break from bottom to top
		// the player that broke the other blocks is not propagated
		// we have to apply a workaround here by having a special "not cloaked" property
		for (int i = 1; i < MAX_GROWTH_HEIGHT_CRYSTAL; i++) {
			BlockPos offsetPos = pos.offset(0, i, 0);
			if (level.getBlockState(offsetPos).is(this)) {
				level.setBlockAndUpdate(offsetPos, level.getBlockState(offsetPos).setValue(ALWAYS_DROP, true));
			} else {
				break;
			}
		}
		
		super.playerDestroy(level, player, pos, state, blockEntity, tool);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		state.getValue(LOGGED).updateShape(state, direction, neighborState, world, pos, neighborPos);
		
		if (!state.canSurvive(world, pos)) {
			world.scheduleTick(pos, this, 1);
		}
		
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(LOGGED).getFluidState();
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE, LOGGED, ALWAYS_DROP);
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.entityInside(state, world, pos, entity);
		state.getValue(LOGGED).onEntityCollision(state, world, pos, entity);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (world.getBlockState(pos.above()).is(this) || !canGrow(world, pos.above())) {
			return;
		}
		
		int height;
		for (height = 1; world.getBlockState(pos.below(height)).is(this); )
			height++;
		
		FluidLogging.State loggingState = world.getBlockState(pos.below(height - 1)).getValue(LOGGED);
		boolean bottomLiquidCrystalLogged = loggingState == FluidLogging.State.LIQUID_CRYSTAL;
		
		// grows taller on liquid crystal
		if (height < MAX_GROWTH_HEIGHT_WATER || (bottomLiquidCrystalLogged && height < MAX_GROWTH_HEIGHT_CRYSTAL)) {
			int age = state.getValue(AGE);
			if (age == 7) {
				// consume 1 block close to the reed when growing.
				// if the quitoxic reeds are growing in liquid crystal: 1/4 chance to consume
				// search for block it could be planted on. 1 block => 1 quitoxic reed
				Optional<BlockPos> posToConsumeBlock = searchConsumableBlock(world, pos.below(height), SpectrumBlockTags.QUITOXIC_REEDS_PLANTABLE, SpectrumBlockTags.QUITOXIC_REEDS_CONSUMABLE, random);
				if (posToConsumeBlock.isEmpty()) {
					return;
				}
				
				if (!bottomLiquidCrystalLogged || random.nextInt(4) == 0) {
					BlockState replacement = switch (loggingState) {
						case FluidLogging.State.LIQUID_CRYSTAL -> SpectrumBlocks.SLUSH.get().defaultBlockState();
						case FluidLogging.State.DRAGONROT -> SpectrumBlocks.ROTTEN_GROUND.get().defaultBlockState();
						default -> Blocks.COARSE_DIRT.defaultBlockState();
					};
					world.setBlockAndUpdate(posToConsumeBlock.get(), replacement);
					world.playSound(null, posToConsumeBlock.get(), SoundEvents.GRAVEL_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
				
				world.setBlockAndUpdate(pos.above(), getStateForPos(world, pos.above()));
				world.setBlock(pos, state.setValue(AGE, 0), 4);
			} else {
				// grow twice as fast, if liquid crystal logged
				if (bottomLiquidCrystalLogged) {
					world.setBlock(pos, state.setValue(AGE, Math.min(7, age + 2)), 4);
				} else {
					world.setBlock(pos, state.setValue(AGE, age + 1), 4);
				}
			}
		}
	}
	
	private Optional<BlockPos> searchConsumableBlock(Level world, BlockPos origin, TagKey<Block> continueTag, TagKey<Block> searchTag, RandomSource random) {
		Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
		AtomicReference<BlockPos> lastFoundPos = new AtomicReference<>(origin);
		BlockPos.breadthFirstTraversal(origin, MAX_CONSUMABLE_BLOCK_SEARCH_DISTANCE, 100,
				(pos, blockPosConsumer) -> {
					if(world.getBlockState(pos).is(searchTag)) {
						lastFoundPos.set(pos);
					}
					blockPosConsumer.accept(pos.relative(direction));
					blockPosConsumer.accept(pos.relative(Direction.DOWN));
					blockPosConsumer.accept(pos.relative(Direction.UP));
				},
				pos -> world.getBlockState(pos).is(continueTag)
		);
		
		if (world.getBlockState(lastFoundPos.get()).is(searchTag)) {
			return Optional.of(lastFoundPos.get());
		} else {
			return Optional.empty();
		}
	}
	
	
	public BlockState getStateForPos(Level world, BlockPos blockPos) {
		FluidLogging.State fluidState = defaultBlockState().getValue(LOGGED).getStateForPos(world, blockPos);
		if(LOGGED.getPossibleValues().contains(fluidState)) {
			return defaultBlockState().setValue(LOGGED, fluidState);
		}
		return defaultBlockState();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityShapeContext) {
			Entity contextEntity = entityShapeContext.getEntity();
			if (contextEntity instanceof Player player) {
				if (this.isVisibleTo(player)) {
					Vec3 vec3d = state.getOffset(world, pos);
					return SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
				} else {
					return Shapes.empty();
				}
			}
		}
		return Shapes.block(); // like breaking particles
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return isValidBlockForPlanting(world, pos);
	}
	
	/**
	 * Can be placed in up to 2 blocks deep water / liquid crystal
	 * growing on SpectrumBlockTags.QUITOXIC_REEDS_PLANTABLE only
	 */
	private boolean isValidBlockForPlanting(LevelReader world, BlockPos pos) {
		BlockState downState = world.getBlockState(pos.below());
		if (downState.is(this)) {
			return true;
		}
		if (!downState.is(SpectrumBlockTags.QUITOXIC_REEDS_PLANTABLE)) {
			return false;
		}
		BlockState upState = world.getBlockState(pos.above());
		BlockState upState2 = world.getBlockState(pos.above(2));
		if (!upState.is(this)) {
			if (!upState.isAir() && !upState2.isAir()) {
				return false;
			}
		}
		
		BlockState state = world.getBlockState(pos);
		if (state.is(this)) {
			return true;
		}
		
		FluidState fluidState = world.getFluidState(pos);
		return fluidState.getAmount() == 8 && (fluidState.is(FluidTags.WATER) || state.is(SpectrumBlocks.LIQUID_CRYSTAL) || state.is(SpectrumBlocks.DRAGONROT));
	}
	
	private boolean canGrow(LevelReader world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.isAir()) {
			return true;
		}
		FluidState fluidState = world.getFluidState(pos);
		return fluidState.getAmount() == 8 && (fluidState.is(FluidTags.WATER) || state.is(SpectrumBlocks.LIQUID_CRYSTAL) || state.is(SpectrumBlocks.DRAGONROT));
	}
	
	@Override
	public float getMaxHorizontalOffset() {
		return 0.15F;
	}
	
	@Override
	public EnumProperty<FluidLogging.State> getFillableFluids() {
		return LOGGED;
	}
	
	@Override
	public EnumProperty<FluidLogging.State> getDrainableFluids() {
		return LOGGED;
	}
	
}
