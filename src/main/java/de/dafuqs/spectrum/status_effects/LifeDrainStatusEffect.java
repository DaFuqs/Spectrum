package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;

import java.util.*;

public class LifeDrainStatusEffect extends SpectrumStatusEffect {
	
	public static final String ATTRIBUTE_UUID_STRING = "28f9e619-20bf-4b2c-9646-06fbf714c00c";
	public static final UUID ATTRIBUTE_UUID = UUID.fromString(ATTRIBUTE_UUID_STRING);
	
	public LifeDrainStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
		if (instance != null) {
			var dragon = entity.getType().isIn(SpectrumEntityTypeTags.DRACONIC);
			EntityAttributeModifier currentMod = instance.getModifier(ATTRIBUTE_UUID);
			if (currentMod != null) {
				instance.removeModifier(currentMod);
				EntityAttributeModifier newModifier = new EntityAttributeModifier(UUID.fromString(ATTRIBUTE_UUID_STRING), this::getTranslationKey, currentMod.getValue() - (dragon ? 2 : 1), EntityAttributeModifier.Operation.ADDITION);
				instance.addPersistentModifier(newModifier);
				instance.getValue(); // recalculate final value
				if (entity.getHealth() > entity.getMaxHealth()) {
					entity.setHealth(entity.getMaxHealth());
				}
			}
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % Math.max(1, 40 - amplifier * 2) == 0;
	}
	
}