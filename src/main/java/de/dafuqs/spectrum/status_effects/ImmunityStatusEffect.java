package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.items.trinkets.WhispyCircletItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ImmunityStatusEffect extends StatusEffect {
	
	public ImmunityStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		WhispyCircletItem.removeNegativeStatusEffects(entity);
	}
	
}