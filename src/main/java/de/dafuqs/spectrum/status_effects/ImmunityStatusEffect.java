package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.items.trinkets.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;

public class ImmunityStatusEffect extends InstantStatusEffect {
	
	public ImmunityStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		WhispyCircletItem.removeNegativeStatusEffects(entity);
	}
	
}