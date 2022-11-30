package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.cca.LastKillComponent;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FrenzyStatusEffect extends SpectrumStatusEffect implements StackableStatusEffect {
	
	public static final String ATTACK_SPEED_UUID_STRING = "7ee7c082-1134-4dc5-b0f9-dab92723f560";
	public static final double ATTACK_SPEED_PER_STAGE = 0.10000000149011612D;
	
	public static final String MOVEMENT_SPEED_UUID_STRING = "a215d081-48a9-4d6c-bdff-a153d4838324";
	public static final double MOVEMENT_SPEED_PER_STAGE = 0.10000000149011612D;
	
	public static final String ATTACK_DAMAGE_UUID_STRING = "061a2c27-eae8-4643-a0c0-0f0d195bc9b1";
	public static final double ATTACK_DAMAGE_PER_STAGE = 0.5D;
	
	public static final String KNOCKBACK_RESISTANCE_UUID_STRING = "b9d38c3a-75b5-462f-a624-eec9b987a5e2";
	public static final double KNOCKBACK_RESISTANCE_PER_STAGE = 0.25D;
	
	public static final long REQUIRE_KILL_EVERY_X_TICKS = 300;
	
	public FrenzyStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
		if (instance != null) {
			long lastKillTickForEntityID = LastKillComponent.getLastKillTick(entity);
			boolean scoredKillInTime = lastKillTickForEntityID >= 0 && entity.getWorld().getTime() - lastKillTickForEntityID < REQUIRE_KILL_EVERY_X_TICKS;
			tick(entity, amplifier, scoredKillInTime);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % REQUIRE_KILL_EVERY_X_TICKS == 0;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (!SpectrumStatusEffects.effectsAreGettingStacked && !entity.hasStatusEffect(this)) {
			super.onApplied(entity, attributes, amplifier);
		}
	}
	
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (!SpectrumStatusEffects.effectsAreGettingStacked) {
			super.onRemoved(entity, attributes, amplifier);
		}
	}
	
	public void tick(@NotNull LivingEntity entity, int amplifier, boolean scoredKillInTimeFrame) {
		AttributeContainer attributes = entity.getAttributes();
		if (attributes != null) {
			for (Map.Entry<EntityAttribute, EntityAttributeModifier> attributeEntry : this.getAttributeModifiers().entrySet()) {
				EntityAttributeInstance entityInstance = attributes.getCustomInstance(attributeEntry.getKey());
				if (entityInstance != null) {
					EntityAttributeModifier baseAttributeValue = attributeEntry.getValue();
					EntityAttributeModifier appliedModifier = entityInstance.getModifier(baseAttributeValue.getId());
					double newBaseValue = appliedModifier == null ? baseAttributeValue.getValue() : appliedModifier.getValue();
					double newValue = this.adjustModifierAmount(newBaseValue, attributeEntry.getValue().getValue(), amplifier, scoredKillInTimeFrame);
					entityInstance.removeModifier(baseAttributeValue);
					entityInstance.addPersistentModifier(new EntityAttributeModifier(baseAttributeValue.getId(), baseAttributeValue.getName(), newValue, baseAttributeValue.getOperation()));
					entityInstance.getValue();
				}
			}
		}
	}
	
	public double adjustModifierAmount(double existingValue, double additionalValue, int amplifier, boolean scoredKillInTimeFrame) {
		return scoredKillInTimeFrame ? existingValue + additionalValue * (amplifier + 1) : existingValue - additionalValue * (amplifier + 1);
	}
	
}
