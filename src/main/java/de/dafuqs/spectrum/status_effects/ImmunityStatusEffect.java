package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.items.trinkets.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;

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
		WhispyCircletItem.removeNegativeStatusEffects(entity);
		return super.applyEffectTick(entity, amplifier);
	}
	
	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		super.onEffectStarted(entity, amplifier);
		WhispyCircletItem.removeNegativeStatusEffects(entity);
	}
	
}