package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.server.network.*;

public class AscensionStatusEffect extends SpectrumStatusEffect {
	
	public static int MUSIC_DURATION_TICKS = 288 * 20;
	public static int MUSIC_INTRO_TICKS = 56 * 20; // 56 seconds
	
	public AscensionStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.world.isClient) {
			ParticleHelper.playParticleWithPatternAndVelocityClient(entity.world, entity.getPos(), SpectrumParticleTypes.WHITE_SPARKLE_RISING, VectorPattern.EIGHT, 0.2);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 4 == 0;
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