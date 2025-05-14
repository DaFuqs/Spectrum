package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;

public class LifeDrainStatusEffect extends MobEffect {
	
	public static final ResourceLocation ATTRIBUTE_ID = SpectrumCommon.locate("effect.life_drain");
	
	public LifeDrainStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
			return true;
		}
		
		AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
		if (instance != null) {
			var dragon = entity.getType().is(SpectrumEntityTypeTags.DRACONIC);
			AttributeModifier currentMod = instance.getModifier(ATTRIBUTE_ID);
			if (currentMod != null) {
				instance.removeModifier(currentMod);
				AttributeModifier newModifier = new AttributeModifier(ATTRIBUTE_ID, currentMod.amount() - (dragon ? 2 : 1), AttributeModifier.Operation.ADD_VALUE);
				instance.addPermanentModifier(newModifier);
				instance.getValue(); // recalculate final value
				if (entity.getHealth() > entity.getMaxHealth()) {
					entity.setHealth(entity.getMaxHealth());
				}
			}
		}
		
		return true;
	}
	
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return duration % Math.max(1, 40 - amplifier * 2) == 0;
	}
	
}
