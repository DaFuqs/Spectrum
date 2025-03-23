package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import org.jetbrains.annotations.*;
import java.util.*;

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
		removeNegativeStatusEffects(entity);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		removeNegativeStatusEffects(entity);
	}

	public static void removeNegativeStatusEffects(@NotNull LivingEntity entity) {
		Set<StatusEffect> effectsToRemove = new HashSet<>();
		Collection<StatusEffectInstance> currentEffects = entity.getStatusEffects();
		for (StatusEffectInstance instance : currentEffects) {
			StatusEffect effectType = instance.getEffectType();
			if (effectType.getCategory() == StatusEffectCategory.HARMFUL && !SpectrumStatusEffectTags.bypassesImmunity(effectType)) {
				effectsToRemove.add(effectType);
			}
		}
		
		for (StatusEffect effect : effectsToRemove) {
			entity.removeStatusEffect(effect);
		}
	}
	
}