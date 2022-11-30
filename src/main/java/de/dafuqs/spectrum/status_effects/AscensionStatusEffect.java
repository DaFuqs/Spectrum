package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.helpers.ParticleHelper;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.ParticlePattern;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;

public class AscensionStatusEffect extends SpectrumStatusEffect {
	
	public static int MUSIC_DURATION_TICKS = 288 * 20;
	public static int MUSIC_INTRO_TICKS = 56 * 20; // 56 seconds
	
	public AscensionStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.world.isClient && entity.world.getTime() % 4 == 0) {
			ParticleHelper.playParticleWithPatternAndVelocityClient(entity.world, entity.getPos(), SpectrumParticleTypes.WHITE_SPARKLE_RISING, ParticlePattern.EIGHT, 0.2);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		if (entity instanceof ServerPlayerEntity player) {
			SpectrumS2CPacketSender.playAscensionAppliedEffects(player);
		}
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onRemoved(entity, attributes, amplifier);
		entity.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.DIVINITY, MUSIC_DURATION_TICKS - MUSIC_INTRO_TICKS, amplifier));
	}
	
}