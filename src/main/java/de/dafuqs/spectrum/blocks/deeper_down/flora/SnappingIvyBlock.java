package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

public class SnappingIvyBlock extends BushBlock implements BonemealableBlock {
	
	public static final MapCodec<SnappingIvyBlock> CODEC = simpleCodec(SnappingIvyBlock::new);
	
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
	public static final BooleanProperty SNAPPED = BooleanProperty.create("snapped");
	
	protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
	protected static final Vec3 MOVEMENT_SLOWDOWN_VECTOR = new Vec3(0.5, 0.75, 0.5);
	
	public SnappingIvyBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X).setValue(SNAPPED, false));
	}
	
	@Override
	public MapCodec<? extends SnappingIvyBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AXIS, SNAPPED);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.is(SpectrumBlockTags.SNAPPING_IVY_PLANTABLE);
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		world.registryAccess()
				.registryOrThrow(Registries.CONFIGURED_FEATURE)
				.get(SpectrumConfiguredFeatureKeys.SNAPPING_IVY_PATCH)
				.place(world, world.getChunkSource().getGenerator(), random, pos);
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(AXIS, state.getValue(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(AXIS, ctx.getHorizontalDirection().getAxis());
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(SNAPPED);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (state.getValue(SNAPPED)) {
			snap(state, world, pos, false);
		}
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		boolean snapped = state.getValue(SNAPPED);
		
		if (!snapped && entity instanceof ItemEntity) {
			snap(state, world, pos, true);
		}
		if (entity instanceof LivingEntity livingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
			entity.makeStuckInBlock(state, MOVEMENT_SLOWDOWN_VECTOR);
			if (!snapped) {
				entity.hurt(SpectrumDamageTypes.snappingIvy(world), 5.0F);
				livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1));
				
				snap(state, world, pos, true);
			}
		}
	}
	
	private static void snap(BlockState state, Level world, BlockPos pos, boolean close) {
		BlockState newState = state.setValue(SNAPPED, close);
		world.setBlock(pos, newState, Block.UPDATE_CLIENTS);
		world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
		world.playSound(null, pos, close ? SoundEvents.BIG_DRIPLEAF_TILT_DOWN : SoundEvents.BIG_DRIPLEAF_TILT_UP, SoundSource.BLOCKS, 1.0F, Mth.randomBetween(world.getRandom(), 0.8F, 1.2F));
	}
	
}
