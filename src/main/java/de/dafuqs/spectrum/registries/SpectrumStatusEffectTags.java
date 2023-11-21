package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.effect.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;


public class SpectrumStatusEffectTags {
	
	public static TagKey<StatusEffect> UNCURABLE;
	public static TagKey<StatusEffect> NO_DURATION_EXTENSION;
	
	public static void register() {
		UNCURABLE = of("uncurable");
		NO_DURATION_EXTENSION = of("no_duration_extension");
	}
	
	private static TagKey<StatusEffect> of(String id) {
		return TagKey.of(RegistryKeys.STATUS_EFFECT, SpectrumCommon.locate(id));
	}
	
	// TODO - Review, upstream handles this by using an Optional Registry Entry
	public static boolean isIn(TagKey<StatusEffect> tag, StatusEffect effect) {
		int id = Registries.STATUS_EFFECT.getRawId(effect);
		var entry = Registries.STATUS_EFFECT.getEntry(id);
		return entry.map(statusEffectRegistryEntry -> statusEffectRegistryEntry.isIn(tag)).orElse(false);
	}
	
	public static boolean isUncurable(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffectTags.UNCURABLE, statusEffect);
	}
	
}
