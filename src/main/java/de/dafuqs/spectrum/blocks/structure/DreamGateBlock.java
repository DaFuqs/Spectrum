package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class DreamGateBlock extends DikeGateBlock {
	
	public static final MapCodec<DreamGateBlock> CODEC = simpleCodec(DreamGateBlock::new);
	
	public DreamGateBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends DreamGateBlock> codec() {
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

				var sleep = SleepStatusEffect.getGeneralSleepResistanceIfEntityHasSoporificEffect(livingEntity);
				if (sleep != -1) {
					if (entity instanceof ServerPlayer player) {
						Support.grantAdvancementCriterion(player, "lategame/enter_strange_preservation_ruin", "enter_dream_gate");
					}
					
					return Shapes.empty();
				}
			}
		}
		return Shapes.block();
	}

	@Override
	public void punishEntityWithoutAzureDike(BlockGetter world, BlockPos pos, Entity entity, boolean decreasedSounds) {
		if (world instanceof ServerLevel serverWorld && entity instanceof LivingEntity livingEntity) {
			
			if (livingEntity instanceof Player player && player.getAbilities().instabuild)
				return;

			var sleep = SleepStatusEffect.getGeneralSleepResistanceIfEntityHasSoporificEffect(livingEntity);
			if (sleep == -1 && serverWorld.getGameTime() % 5 == 0) {
				entity.hurt(SpectrumDamageTypes.sleep(serverWorld, null), 2);
				PlayParticleWithExactVelocityPayload.playParticles(serverWorld, pos, SpectrumParticleTypes.AZURE_DIKE_RUNES, 10);
				if (entity instanceof ServerPlayer serverPlayerEntity && (!decreasedSounds || ((ServerLevel) world).getGameTime() % 10 == 0)) {
					serverPlayerEntity.playNotifySound(SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 0.75F, 1.0F);
				}
			}
		}
	}
}
