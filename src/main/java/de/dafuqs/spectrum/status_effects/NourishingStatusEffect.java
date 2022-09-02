package de.dafuqs.spectrum.status_effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class NourishingStatusEffect extends SpectrumStatusEffect {
	
	public NourishingStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	public String getTranslationKey() {
		return StatusEffects.SATURATION.getTranslationKey();
	}
	
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.world.isClient && entity instanceof PlayerEntity playerEntity) {
			playerEntity.getHungerManager().add(1, 0.25F);
		}
	}
	
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = 200 >> amplifier;
		if(i > 0) {
			return duration % i == 0;
		}
		return true;
	}
	
}