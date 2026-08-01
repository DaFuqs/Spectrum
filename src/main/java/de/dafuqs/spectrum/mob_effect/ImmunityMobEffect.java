package de.dafuqs.spectrum.mob_effect;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.neoforged.neoforge.common.*;

import java.util.*;

public class ImmunityMobEffect extends MobEffect {
	
	public ImmunityMobEffect(MobEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return duration % 20 == 0;
	}
	
	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		removeOtherMobEffects(entity);
		return super.applyEffectTick(entity, amplifier);
	}
	
	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		super.onEffectStarted(entity, amplifier);
		removeOtherMobEffects(entity);
	}
	
	// TODO: can this use an effect cure?
	public static void removeOtherMobEffects(LivingEntity entity) {
		Set<Holder<MobEffect>> effectsToRemove = new HashSet<>();
		for (MobEffectInstance instance : entity.getActiveEffects()) {
			if (!instance.getEffect().is(SpectrumMobEffectTags.BYPASSES_IMMUNITY)) {
				effectsToRemove.add(instance.getEffect());
			}
		}
		
		for (Holder<MobEffect> effect : effectsToRemove) {
			entity.removeEffect(effect);
		}
	}
	
	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
		cures.add(SpectrumEffectCures.COMMAND_ONLY);
	}
	
}