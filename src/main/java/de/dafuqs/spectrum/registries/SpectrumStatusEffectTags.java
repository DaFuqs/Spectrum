package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.effect.*;
import net.minecraft.registry.tag.*;
import net.minecraft.registry.*;


public class SpectrumStatusEffectTags {
	
	public static TagKey<StatusEffect> UNCURABLE;
	public static TagKey<StatusEffect> NO_DURATION_EXTENSION;
	
	private static TagKey<StatusEffect> getReference(String id) {
		// TODO - Is status effect correct here? Should be, although...
		return TagKey.of(RegistryKeys.STATUS_EFFECT, SpectrumCommon.locate(id));
	}
	
	public static void register() {
		UNCURABLE = getReference("uncurable");
		NO_DURATION_EXTENSION = getReference("no_duration_extension");
	}
	
	public static boolean isIn(TagKey<StatusEffect> tag, StatusEffect effect) {
		int id = Registries.STATUS_EFFECT.getRawId(effect);
		var entry = Registries.STATUS_EFFECT.getEntry(id);
		return entry.map(statusEffectRegistryEntry -> statusEffectRegistryEntry.isIn(tag)).orElse(false);
	}
	
	public static boolean isUncurable(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffectTags.UNCURABLE, statusEffect);
	}
	
}
