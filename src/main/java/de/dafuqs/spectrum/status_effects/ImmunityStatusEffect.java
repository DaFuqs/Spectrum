package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.items.trinkets.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;

public class ImmunityStatusEffect extends StatusEffect {
	
	public ImmunityStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 20 == 0;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		super.applyUpdateEffect(entity, amplifier);
		WhispyCircletItem.removeNegativeStatusEffects(entity);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		WhispyCircletItem.removeNegativeStatusEffects(entity);
	}
	
}