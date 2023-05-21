package de.dafuqs.spectrum.status_effects;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;

public class ScarredStatusEffect extends SpectrumStatusEffect {
	
	public ScarredStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		if (entity.isSprinting()) {
			entity.setSprinting(false);
		}
	}
	
	
}
