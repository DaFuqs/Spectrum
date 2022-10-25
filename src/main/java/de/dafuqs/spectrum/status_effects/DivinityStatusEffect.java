package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.items.trinkets.WhispyCircletItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketReceiver;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.ParticlePattern;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class DivinityStatusEffect extends SpectrumStatusEffect {
	
	public DivinityStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if(entity.world.isClient && entity.world.getTime() % 4 == 0) {
			SpectrumS2CPacketReceiver.playParticleWithPatternAndVelocityClient(entity.world, entity.getPos(), SpectrumParticleTypes.RED_CRAFTING, ParticlePattern.EIGHT, 0.2);
		}
		if (entity.getHealth() < entity.getMaxHealth()) {
			entity.heal(amplifier / 2F);
		}
		if (entity instanceof PlayerEntity playerEntity) {
			playerEntity.getHungerManager().add(1, 0.25F);
		}
		if (!entity.world.isClient) {
			WhispyCircletItem.removeSingleHarmfulStatusEffect(entity);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = 80 >> amplifier;
		if (i > 0) {
			return duration % i == 0;
		}
		return true;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		if(entity instanceof ServerPlayerEntity player) {
			SpectrumS2CPacketSender.playDivinityAppliedEffects(player);
		}
	}
	
}