package de.dafuqs.spectrum.mob_effect;

import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import javax.annotation.*;

public class ScarredMobEffect extends MobEffect {
	
	public ScarredMobEffect(MobEffectCategory category, int color) {
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
