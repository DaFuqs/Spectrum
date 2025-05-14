package de.dafuqs.spectrum.status_effects;

import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;

public class ScarredStatusEffect extends MobEffect {
	
	public ScarredStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		super.onEffectStarted(entity, amplifier);
		if (entity.isSprinting()) {
			entity.setSprinting(false);
		}
	}

}
