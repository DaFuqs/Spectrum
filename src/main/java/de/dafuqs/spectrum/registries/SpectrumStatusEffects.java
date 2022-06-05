package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.status_effects.ImmunityStatusEffect;
import de.dafuqs.spectrum.status_effects.NourishingStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Optional;

public class SpectrumStatusEffects {
	
	/**
	 * Clears negative effects on the entity
	 * and makes it immune against new ones
	 */
	public static StatusEffect IMMUNITY;
	/**
	 * Like Saturation, but not OP
	 */
	public static StatusEffect NOURISHING;
	/**
	 * Ouch.
	 */
	//public static StatusEffect MILLENIA_DISEASE;
	
	public static TagKey<StatusEffect> UNCURABLE;
	
	private static StatusEffect registerStatusEffect(String id, StatusEffect entry) {
		return Registry.register(Registry.STATUS_EFFECT, new Identifier(SpectrumCommon.MOD_ID, id), entry);
	}
	
	private static TagKey<StatusEffect> getReference(String id) {
		return TagKey.of(Registry.MOB_EFFECT_KEY, new Identifier(SpectrumCommon.MOD_ID, id));
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
	
	public static void register() {
		IMMUNITY = registerStatusEffect("immunity", new ImmunityStatusEffect(StatusEffectCategory.NEUTRAL, 0x4bbed5));
		NOURISHING = registerStatusEffect("nourishing", new NourishingStatusEffect(StatusEffectCategory.BENEFICIAL, 16262179));
		//MILLENIA_DISEASE = registerStatusEffect("millenia_disease", new MilleniaDiseaseStatusEffect(StatusEffectCategory.NEUTRAL, 0x222222).addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, MilleniaDiseaseStatusEffect.ATTRIBUTE_UUID_STRING, -0.05, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
		
		UNCURABLE = getReference("uncurable");
	}
	
	public static boolean isUncurable(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffects.UNCURABLE, statusEffect);
	}
	
}
