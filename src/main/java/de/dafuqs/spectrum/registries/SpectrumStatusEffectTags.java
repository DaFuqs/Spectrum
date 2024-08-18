package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;

public class SpectrumStatusEffectTags {
	
	public static TagKey<StatusEffect> INCURABLE;
	public static TagKey<StatusEffect> NO_DURATION_EXTENSION;
	public static TagKey<StatusEffect> SOPORIFIC;

	public static void register() {
		INCURABLE = of("uncurable");
		NO_DURATION_EXTENSION = of("no_duration_extension");
		SOPORIFIC = of("soporific");
	}
	
	private static TagKey<StatusEffect> of(String id) {
		return TagKey.of(RegistryKeys.STATUS_EFFECT, SpectrumCommon.locate(id));
	}

	public static boolean isIn(TagKey<StatusEffect> tag, StatusEffect effect) {
		return Registries.STATUS_EFFECT.getEntry(effect).isIn(tag);
	}
	
	public static boolean isIncurable(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffectTags.INCURABLE, statusEffect);
	}
	
	public static boolean hasEffectWithTag(LivingEntity livingEntity, TagKey<StatusEffect> tag) {
		for (StatusEffect statusEffect : livingEntity.getActiveStatusEffects().keySet()) {
			if (SpectrumStatusEffectTags.isIn(tag, statusEffect)) {
				return true;
			}
		}
		return false;
	}
	
}
