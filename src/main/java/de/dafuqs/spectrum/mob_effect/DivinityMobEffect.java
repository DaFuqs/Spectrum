package de.dafuqs.spectrum.mob_effect;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.common.*;
import javax.annotation.*;

import java.util.*;

public class DivinityMobEffect extends MobEffect {
	
	public static final int CIRCLET_AMPLIFIER = 0;
	public static final int ASCENSION_AMPLIFIER = 1;
	
	// since we do not have access to the duration in `applyEffectTick`, we have to store it
	// (since `applyEffectTick` triggers every tick for fancy effects, but we want to proc some effects only sometimes)
	private static int SAVED_DURATION;
	
	public DivinityMobEffect(MobEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		Level world = entity.level();
		if (amplifier > CIRCLET_AMPLIFIER && world.isClientSide) { // the circlet gives divinity 0, not showing effects; the ascension one does
			ParticleHelper.playParticleWithPatternAndVelocityClient(entity.level(), entity.position(), ColoredCraftingParticleEffect.RED, VectorPattern.EIGHT, 0.2);
		}
		
		boolean healAndSaturate = SAVED_DURATION % 80 == 0;
		
		if (entity instanceof Player player) {
			if (!world.isClientSide) {
				SpectrumAdvancementCriteria.DIVINITY_TICK.trigger((ServerPlayer) player);
			}
			if (healAndSaturate) {
				player.getFoodData().eat(1 + amplifier, 0.25F);
			}
		}
		
		if (healAndSaturate) {
			entity.heal(amplifier / 2F);
		}
		
		return super.applyEffectTick(entity, amplifier);
	}
	
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		SAVED_DURATION = duration;
		return true;
	}
	
	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		super.onEffectStarted(entity, amplifier);
		if (entity instanceof Player) {
			if (entity instanceof ServerPlayer player) {
				MobEffectInstance instance = entity.getEffect(SpectrumMobEffects.DIVINITY);
				if (instance != null && !instance.isAmbient()) {
					PlayDivinityAppliedEffectsPayload.playDivinityAppliedEffects(player);
				}
			}
		}
	}
	
	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
		cures.add(SpectrumEffectCures.COMMAND_ONLY);
	}
	
}