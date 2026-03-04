package de.dafuqs.spectrum.mob_effect;

import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

public class NourishingMobEffect extends MobEffect {
	
	public NourishingMobEffect(MobEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public @NotNull String getDescriptionId() {
		return MobEffects.SATURATION.value().getDescriptionId();
	}
	
	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		Level world = entity.level();
		if (!world.isClientSide && entity instanceof Player playerEntity) {
			playerEntity.getFoodData().eat(1, 0.25F);
		}
		return super.applyEffectTick(entity, amplifier);
	}
	
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		int i = 200 >> amplifier;
		if (i > 0) {
			return duration % i == 0;
		}
		return true;
	}
	
}