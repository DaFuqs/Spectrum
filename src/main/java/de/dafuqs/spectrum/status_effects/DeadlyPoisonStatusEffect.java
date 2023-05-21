package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;

public class DeadlyPoisonStatusEffect extends SpectrumStatusEffect {
	
	public DeadlyPoisonStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		entity.damage(SpectrumDamageSources.DEADLY_POISON, 1.0F);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = 25 >> amplifier;
		if (i > 0) {
			return duration % i == 0;
		} else {
			return true;
		}
	}
	
}
