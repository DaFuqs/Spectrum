package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;

public class SpectrumStatusEffectTags {
	
	public static TagKey<StatusEffect> BYPASSES_WHISPY_CIRCLET;
	public static TagKey<StatusEffect> BYPASSES_NECTAR_GLOVES;
	public static TagKey<StatusEffect> BYPASSES_IMMUNITY;
	public static TagKey<StatusEffect> CANNOT_BE_INCURABLE;
	public static TagKey<StatusEffect> NO_DURATION_EXTENSION;
	public static TagKey<StatusEffect> SOPORIFIC;
	public static TagKey<StatusEffect> NIGHT_ALCHEMY;

	public static void register() {
		BYPASSES_WHISPY_CIRCLET = of("bypasses_whispy_circlet");
		BYPASSES_NECTAR_GLOVES = of("bypasses_nectar_gloves");
		BYPASSES_IMMUNITY = of("bypasses_immunity");
		CANNOT_BE_INCURABLE = of("cannot_be_incurable");
		NO_DURATION_EXTENSION = of("no_duration_extension");
		SOPORIFIC = of("soporific");
		NIGHT_ALCHEMY = of("night_alchemy");
	}
	
	private static TagKey<StatusEffect> of(String id) {
		return TagKey.of(RegistryKeys.STATUS_EFFECT, SpectrumCommon.locate(id));
	}

	public static boolean isIn(TagKey<StatusEffect> tag, StatusEffect effect) {
		return Registries.STATUS_EFFECT.getEntry(effect).isIn(tag);
	}
	
	public static boolean bypassesImmunity(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffectTags.BYPASSES_IMMUNITY, statusEffect);
	}

	public static boolean bypassesNectarGloves(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffectTags.BYPASSES_NECTAR_GLOVES, statusEffect);
	}
	
	public static boolean bypassesWhispyCirclet(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffectTags.BYPASSES_WHISPY_CIRCLET, statusEffect);
	}

	public static boolean cannotBeIncurable(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffectTags.CANNOT_BE_INCURABLE, statusEffect);
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
