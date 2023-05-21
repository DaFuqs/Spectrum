package de.dafuqs.spectrum.status_effects;

import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;

public class GravityStatusEffect extends SpectrumStatusEffect {
	
	protected final float gravityPerLevel;
	
	public GravityStatusEffect(StatusEffectCategory statusEffectCategory, int color, float gravityPerLevel) {
		super(statusEffectCategory, color);
		this.gravityPerLevel = gravityPerLevel;
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		applyGravityEffect(entity, gravityPerLevel * (amplifier + 1));
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