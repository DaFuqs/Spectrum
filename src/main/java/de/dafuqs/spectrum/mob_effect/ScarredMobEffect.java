package de.dafuqs.spectrum.mob_effect;

import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.*;

public class ScarredMobEffect extends MobEffect {
	
	public ScarredMobEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public void onEffectStarted(@NotNull LivingEntity entity, int amplifier) {
		super.onEffectStarted(entity, amplifier);
		if (entity.isSprinting()) {
			entity.setSprinting(false);
		}
	}
	
}
