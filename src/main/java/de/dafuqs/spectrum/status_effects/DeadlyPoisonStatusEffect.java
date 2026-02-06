package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.neoforged.neoforge.common.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DeadlyPoisonStatusEffect extends MobEffect {
	
	public DeadlyPoisonStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		entity.hurt(SpectrumDamageTypes.deadlyPoison(entity.level()), 1.0F);
		return super.applyEffectTick(entity, amplifier);
	}
	
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		int i = 25 >> amplifier;
		if (i > 0) {
			return duration % i == 0;
		} else {
			return true;
		}
	}
	
	@Override
	public void fillEffectCures(Set<EffectCure> cures, @NotNull MobEffectInstance effectInstance) {
		super.fillEffectCures(cures, effectInstance);
		
		cures.add(EffectCures.HONEY);
	}
	
}
