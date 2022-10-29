package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Optional;

public class SpectrumStatusEffectTags {
	
	public static TagKey<StatusEffect> UNCURABLE;
	
	private static TagKey<StatusEffect> getReference(String id) {
		return TagKey.of(Registry.MOB_EFFECT_KEY, SpectrumCommon.locate(id));
	}
	
	public static void register() {
		UNCURABLE = getReference("uncurable");
	}
	
	public static boolean isIn(TagKey<StatusEffect> tag, StatusEffect effect) {
		int id = Registry.STATUS_EFFECT.getRawId(effect);
		Optional<RegistryEntry<StatusEffect>> entry = Registry.STATUS_EFFECT.getEntry(id);
		if (entry.isEmpty()) {
			return false;
		} else {
			return entry.get().isIn(tag);
		}
	}
	
	public static boolean isUncurable(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffectTags.UNCURABLE, statusEffect);
	}
	
}
