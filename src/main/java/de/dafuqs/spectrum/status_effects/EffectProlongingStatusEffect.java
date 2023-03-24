package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;

public class EffectProlongingStatusEffect extends SpectrumStatusEffect implements StackableStatusEffect {
	
	public static final float ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL = 0.25F;
	
	public EffectProlongingStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
	
	}
	
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
	
	}
	
	public static boolean canBeExtended(StatusEffect statusEffect) {
		return !SpectrumStatusEffectTags.isIn(SpectrumStatusEffectTags.NO_DURATION_EXTENSION, statusEffect);
	}
	
	public static int getExtendedDuration(int originalDuration, int prolongingAmplifier) {
		return (int) (originalDuration * (1 + ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL * prolongingAmplifier));
	}
	
}
