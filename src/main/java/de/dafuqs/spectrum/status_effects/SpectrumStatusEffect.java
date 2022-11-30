package de.dafuqs.spectrum.status_effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.jetbrains.annotations.Nullable;

public class SpectrumStatusEffect extends StatusEffect {
	
	public SpectrumStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}
	
	// no unused super() calls (performance)
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return false;
	}
	
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
	
	}
	
	public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
	
	}
	
	
}
