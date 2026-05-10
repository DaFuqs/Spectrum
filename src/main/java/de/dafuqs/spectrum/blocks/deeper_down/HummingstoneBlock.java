package de.dafuqs.spectrum.blocks.deeper_down;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

public class HummingstoneBlock extends BaseEntityBlock {
	
	public static final MapCodec<HummingstoneBlock> CODEC = simpleCodec(HummingstoneBlock::new);
	
	public static final float CHANCE_TO_ECHO_HUM_EVENT = 0.08F;
	public static final BooleanProperty HUMMING = BooleanProperty.create("humming");
	
	public HummingstoneBlock(Properties settings) {
		super(settings);
		registerDefaultState(this.stateDefinition.any().setValue(HUMMING, false));
	}
	
	@Override
	public MapCodec<? extends HummingstoneBlock> codec() {
		return CODEC;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		
		if (!state.getValue(HUMMING)) {
			return;
		}
		
		// particles
		Direction direction = Direction.getRandom(random);
		if (direction != Direction.DOWN) {
			BlockPos blockPos = pos.relative(direction);
			BlockState blockState = world.getBlockState(blockPos);
			if (!state.canOcclude() || !blockState.isFaceSturdy(world, blockPos, direction.getOpposite())) {
				double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepX() * 0.6D;
				double e = direction.getStepY() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepY() * 0.6D;
				double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepZ() * 0.6D;
				world.addParticle(ParticleTypes.NOTE, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0D, 0.05D, 0.0D);
			}
		}
		
		// sound
		float r = random.nextFloat();
		if (r < 0.3F) {
			float pitch = 0.4F + 0.4F * pos.getX() % 8 + 0.4F * pos.getY() % 8 + 0.4F * pos.getZ() % 8;
			world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectrumSoundEvents.HUMMINGSTONE_HUM, SoundSource.BLOCKS, 0.4F + random.nextFloat() * 0.1F, pitch, false);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HUMMING);
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (!state.getValue(HUMMING)) {
			if (!world.isClientSide()) {
				startHumming(world, pos, state, player, false);
			}
			return InteractionResult.sidedSuccess(world.isClientSide());
		}
		return super.useWithoutItem(state, world, pos, player, hit);
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(HUMMING);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.randomTick(state, world, pos, random);
		if (!world.isClientSide() && state.getValue(HUMMING)) {
			stopHumming(world, pos, state);
		}
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.entityInside(state, world, pos, entity);
		if (!world.isClientSide() && !state.getValue(HUMMING)) {
			startHumming(world, pos, state, entity, false);
		}
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		super.fallOn(world, state, pos, entity, fallDistance);
		if (!world.isClientSide() && !state.getValue(HUMMING)) {
			startHumming(world, pos, state, entity, false);
		}
	}
	
	@Override
	public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (!world.isClientSide() && !state.getValue(HUMMING)) {
			startHumming(world, hit.getBlockPos(), state, projectile.getOwner(), false);
		}
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide() ? null : createTickerHelper(type, SpectrumBlockEntities.HUMMINGSTONE.get(), HummingstoneBlockEntity::serverTick);
	}
	
	@Override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(HUMMING);
	}
	
	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return state.getValue(HUMMING) ? 15 : 0;
	}
	
	public static void startHumming(Level world, BlockPos pos, BlockState state, @Nullable Entity entity, boolean causedByOtherHum) {
		if (!(state.getBlock() instanceof HummingstoneBlock)) {
			return;
		}
		
		world.playSound(null, pos, SpectrumSoundEvents.HUMMINGSTONE_HUM, SoundSource.BLOCKS, 0.75F, 1.0F);
		if (!state.getValue(HUMMING)) {
			world.setBlockAndUpdate(pos, state.setValue(HUMMING, true));
		}
		if (!causedByOtherHum || world.getRandom().nextFloat() < CHANCE_TO_ECHO_HUM_EVENT) {
			world.gameEvent(entity, SpectrumGameEvents.HUMMINGSTONE_HUMMING, pos);
		}
	}
	
	public static void stopHumming(Level world, BlockPos pos, BlockState state) {
		world.setBlockAndUpdate(pos, state.setValue(HUMMING, false));
		world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.5F, 0.5F + world.getRandom().nextFloat() * 1.2F);
	}
	
	public static void onHymn(Level world, BlockPos pos, @Nullable Entity entity) {
		if (!(world.getBlockState(pos).getBlock() instanceof HummingstoneBlock)) {
			return;
		}
		
		world.gameEvent(entity, SpectrumGameEvents.HUMMINGSTONE_HYMN, pos);
		world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.25F, 0.5F + world.getRandom().nextFloat() * 1.2F);
		world.destroyBlock(pos, false);
		popResource(world, pos, SpectrumItems.RESONANCE_SHARD.get().getDefaultInstance());
		
		if (entity instanceof ServerPlayer serverPlayerEntity) {
			SpectrumAdvancementCriteria.HUMMINGSTONE_HYMN.trigger(serverPlayerEntity, (ServerLevel) world, pos);
		}
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HummingstoneBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable GameEventListener getListener(ServerLevel world, T blockEntity) {
		if (blockEntity instanceof HummingstoneBlockEntity hummingstoneBlockEntity) {
			return hummingstoneBlockEntity.listener;
		}
		return null;
	}
	
}
