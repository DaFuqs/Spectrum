package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class DreamGateBlock extends DikeGateBlock {

	public DreamGateBlock(Settings settings) {
		super(settings);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity instanceof LivingEntity livingEntity) {

				if (entity instanceof PlayerEntity player && player.isCreative()) {
					return VoxelShapes.empty();
				}

				var sleep = SleepStatusEffect.getGeneralSleepResistanceIfEntityHasSoporificEffect(livingEntity);
				if (sleep != -1) {
					if (entity instanceof ServerPlayerEntity player) {
						Support.grantAdvancementCriterion(player, "lategame/enter_strange_preservation_ruin", "enter_dream_gate");
					}

					return VoxelShapes.empty();
				}
			}
		}
		return VoxelShapes.fullCube();
	}

	@Override
	public void punishEntityWithoutAzureDike(BlockView world, BlockPos pos, Entity entity, boolean decreasedSounds) {
		if (world instanceof ServerWorld serverWorld && entity instanceof LivingEntity livingEntity) {

			if (livingEntity instanceof PlayerEntity player && player.getAbilities().creativeMode)
				return;

			var sleep = SleepStatusEffect.getGeneralSleepResistanceIfEntityHasSoporificEffect(livingEntity);
			if ( sleep == -1 && serverWorld.getTime() % 5 == 0) {
				entity.damage(SpectrumDamageTypes.sleep(serverWorld, null), 2);
				SpectrumS2CPacketSender.playParticles(serverWorld, pos, SpectrumParticleTypes.AZURE_DIKE_RUNES, 10);
				if (entity instanceof ServerPlayerEntity serverPlayerEntity && (!decreasedSounds || ((ServerWorld) world).getTime() % 10 == 0)) {
					serverPlayerEntity.playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 0.75F, 1.0F);
				}
			}
		}
	}
}
