package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.status_effects.ImmunityStatusEffect;
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
	
	private static StatusEffect registerStatusEffect(String id, StatusEffect entry) {
		return Registry.register(Registry.STATUS_EFFECT, new Identifier(SpectrumCommon.MOD_ID, id), entry);
	}
	
	public static void register() {
		IMMUNITY = registerStatusEffect("immunity", new ImmunityStatusEffect(StatusEffectCategory.NEUTRAL, 0x4bbed5));
	}
	
}
