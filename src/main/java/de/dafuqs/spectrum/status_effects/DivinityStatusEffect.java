package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import net.fabricmc.loader.api.*;
import net.minecraft.client.network.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.network.*;
import net.minecraft.world.*;

public class DivinityStatusEffect extends SpectrumStatusEffect {
	
	public DivinityStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		World world = entity.getWorld();
		if (world.isClient) {
			ParticleHelper.playParticleWithPatternAndVelocityClient(entity.world, entity.getPos(), SpectrumParticleTypes.RED_CRAFTING, ParticlePattern.EIGHT, 0.2);
		}
		if (entity instanceof PlayerEntity player) {
			if (!world.isClient) {
				SpectrumAdvancementCriteria.DIVINITY_TICK.trigger((ServerPlayerEntity) player);
			}
			if(world.getTime() % 20 == 0) {
				player.getHungerManager().add(1, 0.25F);
			}
		}

		if(world.getTime() % 20 == 0) {
			if (entity.getHealth() < entity.getMaxHealth()) {
				entity.heal(amplifier / 2F);
			}
		}
		if(world.getTime() % 200 == 0) {
			WhispyCircletItem.removeSingleStatusEffect(entity, StatusEffectCategory.HARMFUL);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		if (entity instanceof ServerPlayerEntity player) {
			SpectrumS2CPacketSender.playDivinityAppliedEffects(player);
		}
		if (entity instanceof ClientPlayerEntity) {
			FabricLoader.getInstance().getObjectShare().put("healthoverlay:forceHardcoreHearts", true);
		}
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onRemoved(entity, attributes, amplifier);
		if (entity instanceof ClientPlayerEntity) {
			FabricLoader.getInstance().getObjectShare().put("healthoverlay:forceHardcoreHearts", false);
		}
	}

}