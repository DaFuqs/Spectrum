package de.dafuqs.spectrum.recipe.potion_workshop;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class StatusEffectHelper {
	
	public static HashMap<StatusEffect, StatusEffect> negativeToPositiveEffect = new HashMap<>() {{
		put(StatusEffects.BAD_OMEN, StatusEffects.HERO_OF_THE_VILLAGE);
		put(StatusEffects.HUNGER, StatusEffects.SATURATION);
		put(StatusEffects.INSTANT_DAMAGE, StatusEffects.INSTANT_HEALTH);
		put(StatusEffects.MINING_FATIGUE, StatusEffects.HASTE);
		put(StatusEffects.SLOWNESS, StatusEffects.SPEED);
		put(StatusEffects.UNLUCK, StatusEffects.LUCK);
		put(StatusEffects.WEAKNESS, StatusEffects.STRENGTH);
		put(StatusEffects.WITHER, StatusEffects.REGENERATION);
	}};
	
	public static @Nullable StatusEffect getPositiveVariant(@NotNull StatusEffect statusEffect) {
		if (statusEffect.getCategory() == StatusEffectCategory.HARMFUL) {
			return negativeToPositiveEffect.getOrDefault(statusEffect, null);
		} else {
			return statusEffect;
		}
	}
	
}
