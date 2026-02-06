package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.neoforged.neoforge.common.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ImmunityStatusEffect extends MobEffect {
	
	public ImmunityStatusEffect(MobEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return duration % 20 == 0;
	}
	
	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		removeOtherStatusEffects(entity);
		return super.applyEffectTick(entity, amplifier);
	}
	
	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		super.onEffectStarted(entity, amplifier);
		removeOtherStatusEffects(entity);
	}
	
	public static void removeOtherStatusEffects(@NotNull LivingEntity entity) {
		Set<Holder<MobEffect>> effectsToRemove = new HashSet<>();
		for (MobEffectInstance instance : entity.getActiveEffects()) {
			if (!instance.getEffect().is(SpectrumStatusEffectTags.BYPASSES_IMMUNITY)) {
				effectsToRemove.add(instance.getEffect());
			}
		}
		
		for (Holder<MobEffect> effect : effectsToRemove) {
			entity.removeEffect(effect);
		}
	}
	
	@Override
	public void fillEffectCures(Set<EffectCure> cures, @NotNull MobEffectInstance effectInstance) {
		cures.add(SpectrumStatusEffectCures.COMMAND_ONLY);
	}
	
}