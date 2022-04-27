package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.status_effects.ImmunityStatusEffect;
import de.dafuqs.spectrum.status_effects.MilleniaDiseaseStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumStatusEffects {
	
	/**
	 * Clears negative effects on the entity
	 * and makes it immune against new ones
	 */
	public static StatusEffect IMMUNITY;
	/**
	 * Ouch.
	 */
	public static StatusEffect MILLENIA_DISEASE;
	
	private static StatusEffect registerStatusEffect(String id, StatusEffect entry) {
		return Registry.register(Registry.STATUS_EFFECT, new Identifier(SpectrumCommon.MOD_ID, id), entry);
	}
	
	public static void register() {
		IMMUNITY = registerStatusEffect("immunity", new ImmunityStatusEffect(StatusEffectCategory.NEUTRAL, 0x4bbed5));
		MILLENIA_DISEASE = registerStatusEffect("millenia_disease", new MilleniaDiseaseStatusEffect(StatusEffectCategory.NEUTRAL, 0x222222).addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, MilleniaDiseaseStatusEffect.ATTRIBUTE_UUID_STRING, -0.05, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
	}
	
}
