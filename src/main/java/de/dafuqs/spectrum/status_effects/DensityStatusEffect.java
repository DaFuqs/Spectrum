package de.dafuqs.spectrum.status_effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;

public class DensityStatusEffect extends SpectrumStatusEffect {
	
	public static final float GRAVITY_PER_LEVEL = -0.02F;
	
	public DensityStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		applyGravityEffect(entity, GRAVITY_PER_LEVEL * (amplifier + 1));
	}
	
	public static void applyGravityEffect(Entity entity, double additionalYVelocity) {
		// don't affect creative/spectators/... players or immune boss mobs
		if (entity.isPushable() && !(entity).isSpectator()) {
			if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()) {
				// do not affect those
			} else {
				entity.addVelocity(0, additionalYVelocity, 0);
				// maybe add fall distance, when not touching the ground?
			}
		}
	}
	
}