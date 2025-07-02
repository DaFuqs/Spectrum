package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

import java.util.*;

public class DikeGateBlock extends TransparentBlock {
	
	public static final MapCodec<DikeGateBlock> CODEC = simpleCodec(DikeGateBlock::new);
	
	public DikeGateBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends DikeGateBlock> codec() {
		return CODEC;
	}
	
	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity instanceof LivingEntity livingEntity) {
				
				if (entity instanceof Player player && player.isCreative()) {
					return Shapes.empty();
				}
				
				var charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
				if (charges > 0) {
					return Shapes.empty();
				}
			}
		}
		return Shapes.block();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context.isHoldingItem(this.asItem())) {
			return Shapes.block();
		} else {
			return getCollisionShape(state, world, pos, context);
		}
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		punishEntityWithoutAzureDike(world, pos, player, false);
		return super.useWithoutItem(state, world, pos, player, hit);
	}
	
	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter world, BlockPos pos) {
		punishEntityWithoutAzureDike(world, pos, player, true);
		return super.getDestroyProgress(state, player, world, pos);
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		punishEntityWithoutAzureDike(world, pos, entity, true);
		super.entityInside(state, world, pos, entity);
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		Iterator<Direction> directions = Util.shuffledCopy(Direction.values(), random).iterator();
		for (int i = 0; i < 2; i++) {
			Direction direction = directions.next();
			BlockPos blockPos = pos.relative(direction);
			BlockState blockState = world.getBlockState(blockPos);
			if (!state.canOcclude() || !blockState.isFaceSturdy(world, blockPos, direction.getOpposite())) {
				double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5 + direction.getStepX() * 0.6;
				double e = direction.getStepY() == 0 ? random.nextDouble() : 0.5 + direction.getStepY() * 0.6;
				double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + direction.getStepZ() * 0.6;
				world.addParticle(SpectrumParticleTypes.AZURE_DIKE_RUNES, pos.getX() + d, pos.getY() + e, pos.getZ() + f, 0.0, 0.025, 0.0);
			}
		}
	}
	
	public void punishEntityWithoutAzureDike(BlockGetter world, BlockPos pos, Entity entity, boolean decreasedSounds) {
		if (world instanceof ServerLevel serverWorld && entity instanceof LivingEntity livingEntity) {
			int charges = (int) Math.ceil(AzureDikeProvider.getAzureDikeCharges(livingEntity));
			if (charges == 0) {
				entity.hurt(SpectrumDamageTypes.dike(serverWorld), 1);
				PlayParticleWithExactVelocityPayload.playParticles(serverWorld, pos, SpectrumParticleTypes.AZURE_DIKE_RUNES, 10);
				if (entity instanceof ServerPlayer serverPlayerEntity && (!decreasedSounds || ((ServerLevel) world).getGameTime() % 10 == 0)) {
					serverPlayerEntity.playNotifySound(SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 0.75F, 1.0F);
				}
			}
		}
	}
	
}
