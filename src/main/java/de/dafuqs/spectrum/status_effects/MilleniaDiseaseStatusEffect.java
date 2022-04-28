/*package de.dafuqs.spectrum.status_effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class MilleniaDiseaseStatusEffect extends StatusEffect {
	
	public static final String ATTRIBUTE_UUID_STRING = "28f9e619-20bf-4b2c-9646-06fbf714c00c";
	public static final UUID ATTRIBUTE_UUID = UUID.fromString(ATTRIBUTE_UUID_STRING);
	
	public MilleniaDiseaseStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		// TODO: make this work
		if(!entity.world.isClient) {
			EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
			if (instance != null) {
				EntityAttributeModifier currentMod = instance.getModifier(ATTRIBUTE_UUID);
				if (currentMod != null) {
					instance.removeModifier(currentMod);
					EntityAttributeModifier newModifier = new EntityAttributeModifier(UUID.fromString(ATTRIBUTE_UUID_STRING), this::getTranslationKey, currentMod.getValue() - 0.05, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
					instance.addPersistentModifier(newModifier);
					instance.getValue();
				}
			}
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % Math.max(1, 20 - amplifier * 2) == 0;
	}
	
}
*/