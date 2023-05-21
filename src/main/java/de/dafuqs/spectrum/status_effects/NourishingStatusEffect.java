package de.dafuqs.spectrum.status_effects;

import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;

public class NourishingStatusEffect extends SpectrumStatusEffect {
	
	public NourishingStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public String getTranslationKey() {
		return StatusEffects.SATURATION.getTranslationKey();
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.world.isClient && entity instanceof PlayerEntity playerEntity) {
			playerEntity.getHungerManager().add(1, 0.25F);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = 200 >> amplifier;
		if (i > 0) {
			return duration % i == 0;
		}
		return true;
	}
	
}