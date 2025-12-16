package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.loader.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.network.*;
import net.minecraft.world.*;

public class DivinityStatusEffect extends SpectrumStatusEffect {
	
	public static final int CIRCLET_AMPLIFIER = 0;
	public static final int ASCENSION_AMPLIFIER = 1;
	
	// since we do not have access to the duration in `applyEffectTick`, we have to store it
	// (since `applyEffectTick` triggers every tick for fancy effects, but we want to proc some effects only sometimes)
	private static int SAVED_DURATION;

	public DivinityStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		World world = entity.getWorld();
		if (amplifier > CIRCLET_AMPLIFIER && world.isClient) { // the circlet gives divinity 0, not showing effects; the ascension one does
			ParticleHelper.playParticleWithPatternAndVelocityClient(entity.getWorld(), entity.getPos(), SpectrumParticleTypes.RED_CRAFTING, VectorPattern.EIGHT, 0.2);
		}
		boolean healAndSaturate = SAVED_DURATION % 80 == 0;
		if (entity instanceof PlayerEntity player) {
			if (!world.isClient) {
				SpectrumAdvancementCriteria.DIVINITY_TICK.trigger((ServerPlayerEntity) player);
			}
			if (healAndSaturate) {
				player.getHungerManager().add(1 + amplifier, 0.25F);
			}
		}

		if (healAndSaturate) {
			entity.heal(amplifier / 2F);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		SAVED_DURATION = duration;
		return true;
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		World world = entity.getWorld();
		if (entity instanceof PlayerEntity) {
			if (entity instanceof ServerPlayerEntity player) {
				StatusEffectInstance instance = entity.getStatusEffect(SpectrumStatusEffects.DIVINITY);
				if (instance != null && !instance.isAmbient()) {
					SpectrumS2CPacketSender.playDivinityAppliedEffects(player);
				}
			} else if (world.isClient) {
				FabricLoader.getInstance().getObjectShare().put("healthoverlay:forceHardcoreHearts", true);
			}
		}
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onRemoved(entity, attributes, amplifier);
		World world = entity.getWorld();
		if (world.isClient) {
			FabricLoader.getInstance().getObjectShare().put("healthoverlay:forceHardcoreHearts", false);
		}
	}

}