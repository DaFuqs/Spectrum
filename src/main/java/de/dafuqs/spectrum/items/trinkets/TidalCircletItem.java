package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class TidalCircletItem extends SpectrumTrinketItem {
	
	private final int TRIGGER_EVERY_X_TICKS = 40;
	private final int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 10;
	
	private final int HEAL_AXOLOTLS_EVERY_X_TICKS = 160;
	private final int MAX_AXOLOTL_DISTANCE = 12;
	private final int AXOLOTL_HEALING = 2;
	
	public TidalCircletItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_tidal_circlet"));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.tidal_circlet.tooltip").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.tidal_circlet.tooltip2").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.tidal_circlet.tooltip3").formatted(Formatting.GRAY));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		
		if (!entity.getWorld().isClient) {
			long time = entity.getWorld().getTime();
			if (entity.isSubmergedInWater()) {
				if (time % TRIGGER_EVERY_X_TICKS == 0) {
					giveEffects(entity);
				}
				if (time % HEAL_AXOLOTLS_EVERY_X_TICKS == 0 && entity instanceof ServerPlayerEntity serverPlayerEntity) {
					healLovingAxolotls(serverPlayerEntity);
				}
			}
		}
	}
	
	private void giveEffects(@NotNull LivingEntity entity) {
		entity.setAir(entity.getMaxAir());
		Map<StatusEffect, StatusEffectInstance> effects = entity.getActiveStatusEffects();
		if (!effects.containsKey(StatusEffects.DOLPHINS_GRACE) || effects.get(StatusEffects.DOLPHINS_GRACE).getDuration() < TRIGGER_EVERY_X_TICKS) {
			entity.removeStatusEffect(StatusEffects.DOLPHINS_GRACE);
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, EFFECT_DURATION, 1, true, true));
		}
		if (!effects.containsKey(StatusEffects.CONDUIT_POWER) || effects.get(StatusEffects.CONDUIT_POWER).getDuration() < TRIGGER_EVERY_X_TICKS) {
			entity.removeStatusEffect(StatusEffects.CONDUIT_POWER);
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, EFFECT_DURATION, 0, true, true));
		}
	}
	
	private void healLovingAxolotls(@NotNull ServerPlayerEntity entity) {
		List<AxolotlEntity> nearbyAxolotls = entity.getWorld().getEntitiesByType(EntityType.AXOLOTL, Box.of(entity.getPos(), MAX_AXOLOTL_DISTANCE, MAX_AXOLOTL_DISTANCE, MAX_AXOLOTL_DISTANCE), LivingEntity::isAlive);
		for (AxolotlEntity axolotlEntity : nearbyAxolotls) {
			if (axolotlEntity.getHealth() < axolotlEntity.getMaxHealth() && axolotlEntity.getLovingPlayer() != null && axolotlEntity.getLovingPlayer().equals(entity)) {
				axolotlEntity.heal(AXOLOTL_HEALING);
				entity.playSound(SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME, SoundCategory.NEUTRAL, 1.0F, 0.9F + entity.getWorld().random.nextFloat() * 0.2F);
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) axolotlEntity.getWorld(), axolotlEntity.getPos(), ParticleTypes.WAX_OFF, 10, new Vec3d(0.5, 0.5, 0.5), new Vec3d(0, 0, 0));
			}
		}
	}
	
}