package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class WeepingCircletItem extends SpectrumTrinketItem {
	
	private final static int TRIGGER_EVERY_X_TICKS = 40;
	private final static int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 10;
	
	private final static int HEAL_AXOLOTLS_EVERY_X_TICKS = 160;
	private final static int MAX_AXOLOTL_DISTANCE = 12;
	private final static int AXOLOTL_HEALING = 2;
	
	public WeepingCircletItem(Settings settings) {
		super(settings, SpectrumCommon.locate("progression/unlock_weeping_circlet"));
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.weeping_circlet.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.weeping_circlet.tooltip2").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.weeping_circlet.tooltip3").formatted(Formatting.GRAY));
	}

	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onEquip(stack, slot, entity);
		doEffects(entity, true);
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		doEffects(entity, false);
	}
	
	private void doEffects(LivingEntity entity, boolean always) {
		if (!entity.getWorld().isClient) {
			long time = entity.getWorld().getTime();
			if (entity.isSubmergedIn(SpectrumFluidTags.ACTIVATES_WEEPING_CIRCLET)) {
				if (always || time % TRIGGER_EVERY_X_TICKS == 0) {
					entity.setAir(entity.getMaxAir());
					entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, EFFECT_DURATION, 1, true, true));
					entity.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, EFFECT_DURATION, 0, true, true));
				}
				if ((always || time % HEAL_AXOLOTLS_EVERY_X_TICKS == 0) && entity instanceof ServerPlayerEntity serverPlayerEntity) {
					healLovingAxolotls(serverPlayerEntity);
				}
			}
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