package de.dafuqs.spectrum.blocks.rock_candy;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.redstone.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SugarStickBlock extends Block implements RockCandy {
	
	protected final static Map<RockCandyVariant, Block> SUGAR_STICK_BLOCKS = new EnumMap<>(RockCandyVariant.class);
	
	protected final RockCandyVariant rockCandyVariant;
	
	public static final int ITEM_SEARCH_RANGE = 5;
	public static final int REQUIRED_ITEM_COUNT_PER_STAGE = 4;
	
	public static final EnumProperty<FluidLogging.State> LOGGED = FluidLogging.NONE_AND_CRYSTAL;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
	
	protected static final VoxelShape SHAPE = Block.box(5.0D, 3.0D, 5.0D, 11.0D, 16.0D, 11.0D);
	
	public SugarStickBlock(BlockBehaviour.Properties settings, RockCandyVariant rockCandyVariant) {
		super(settings);
		this.rockCandyVariant = rockCandyVariant;
		SUGAR_STICK_BLOCKS.put(this.rockCandyVariant, this);
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(LOGGED, FluidLogging.State.NOT_LOGGED));
	}
	
	@Override
	public MapCodec<? extends BlockBreakerBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public RockCandyVariant getVariant() {
		return this.rockCandyVariant;
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		if (fluidState.getType() == SpectrumFluids.LIQUID_CRYSTAL) {
			return super.getStateForPlacement(ctx).setValue(LOGGED, FluidLogging.State.LIQUID_CRYSTAL);
		} else {
			return super.getStateForPlacement(ctx);
		}
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(LOGGED).isOf(SpectrumFluids.LIQUID_CRYSTAL) ? SpectrumFluids.LIQUID_CRYSTAL.getSource(false) : super.getFluidState(state);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE, LOGGED);
	}
	
	@Override
	protected boolean isRandomlyTicking(BlockState state) {
		return state.getValue(LOGGED).isOf(SpectrumFluids.LIQUID_CRYSTAL) && state.getValue(AGE) < BlockStateProperties.MAX_AGE_2;
	}
	
	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		super.tick(state, level, pos, random);
		if (state.getValue(LOGGED).isOf(Fluids.EMPTY)) {
			int age = state.getValue(AGE);
			
			if (age == 2 || (age == 1 ? random.nextBoolean() : random.nextFloat() < 0.25)) {
				level.addParticle(new DynamicParticleEffect(0.1F, SpectrumColorHelper.getRGBVec(rockCandyVariant.getDyeColor()), 0.5F, 120, true, true),
						pos.getX() + 0.25 + random.nextFloat() * 0.5,
						pos.getY() + 0.25 + random.nextFloat() * 0.5,
						pos.getZ() + 0.25 + random.nextFloat() * 0.5,
						0.08 - random.nextFloat() * 0.16,
						0.04 - random.nextFloat() * 0.16,
						0.08 - random.nextFloat() * 0.16);
			}
			
		}
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.randomTick(state, world, pos, random);
		
		if (state.getValue(LOGGED).isOf(SpectrumFluids.LIQUID_CRYSTAL)) {
			int age = state.getValue(AGE);
			if (age < BlockStateProperties.MAX_AGE_2) {
				List<ItemEntity> itemEntities = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(Vec3.atCenterOf(pos), ITEM_SEARCH_RANGE, ITEM_SEARCH_RANGE, ITEM_SEARCH_RANGE));
				Collections.shuffle(itemEntities);
				for (ItemEntity itemEntity : itemEntities) {
					// is the item also submerged?
					// lazy, but mostly accurate and performant way to check if it's the same liquid pool
					if (!itemEntity.isEyeInFluid(SpectrumFluidTags.LIQUID_CRYSTAL)) {
						continue;
					}
					
					ItemStack stack = itemEntity.getItem();
					if (stack.getCount() >= REQUIRED_ITEM_COUNT_PER_STAGE) {
						@Nullable RockCandyVariant itemVariant = RockCandyVariant.getFor(stack);
						if (itemVariant != null) {
							BlockState newState;
							if (rockCandyVariant != RockCandyVariant.SUGAR) {
								newState = state;
							} else {
								newState = SUGAR_STICK_BLOCKS.get(itemVariant).defaultBlockState();
							}
							
							stack.shrink(REQUIRED_ITEM_COUNT_PER_STAGE);
							world.setBlockAndUpdate(pos, newState.setValue(AGE, age + 1).setValue(LOGGED, state.getValue(LOGGED)));
							world.playSound(null, pos, newState.getSoundType().getHitSound(), SoundSource.BLOCKS, 0.5F, 1.0F);
							break;
						}
					}
				}
			}
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Direction direction = Direction.UP;
		return Block.canSupportCenter(world, pos.relative(direction), direction.getOpposite());
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.UP && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		BlockItemStateProperties stateComponent = stack.get(DataComponents.BLOCK_STATE);
		if (stateComponent != null) {
			Integer age = stateComponent.get(SugarStickBlock.AGE);
			switch (age) {
				case 1 -> {
					tooltip.add(Component.translatable("block.spectrum.sugar_stick.tooltip.medium"));
				}
				case 2 -> {
					tooltip.add(Component.translatable("block.spectrum.sugar_stick.tooltip.large"));
				}
				case null, default -> {
				}
			}
		}
	}
	
}