package de.dafuqs.spectrum.mob_effect;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.neoforged.neoforge.common.*;

import java.util.*;

public class AscensionMobEffect extends MobEffect {
	
	public static final int MUSIC_DURATION_TICKS = 288 * 20;
	public static final int MUSIC_INTRO_TICKS = 1130; // 56.5 seconds
	
	private static boolean applyDivinity = false;
	
	public AscensionMobEffect(MobEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            ParticleHelper.playParticleWithPatternAndVelocityClient(entity.level(), entity.position(), ColoredSparkleRisingParticleEffect.WHITE, VectorPattern.EIGHT, 0.2);
        } else if (applyDivinity) {
            entity.addEffect(new MobEffectInstance(SpectrumMobEffects.DIVINITY, MUSIC_DURATION_TICKS - MUSIC_INTRO_TICKS, DivinityMobEffect.ASCENSION_AMPLIFIER));
            return false;
        }
		return super.applyEffectTick(entity, amplifier);
	}
	
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		applyDivinity = duration == 1;
		return applyDivinity || duration % 4 == 0;
	}
	
	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		super.onEffectStarted(entity, amplifier);
		if (entity instanceof ServerPlayer player) {
			PlayAscensionAppliedEffectsPayload.playAscensionAppliedEffects(player);
		}
	}
	
	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
		cures.add(SpectrumEffectCures.COMMAND_ONLY);
	}
	
}