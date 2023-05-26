package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.items.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionPendantItem extends SpectrumTrinketItem implements InkPoweredPotionFillable {
	
	private final static int TRIGGER_EVERY_X_TICKS = 300;
	private final static int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 220; // always keeps the effect active & prevents the 10 seconds of screen flashing when night vision runs out
	
	private final int maxEffectCount;
	private final int maxAmplifier;
	
	public PotionPendantItem(Settings settings, int maxEffectCount, int maxAmplifier, Identifier unlockIdentifier) {
		super(settings, unlockIdentifier);
		this.maxEffectCount = maxEffectCount;
		this.maxAmplifier = maxAmplifier;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		appendPotionFillableTooltip(stack, tooltip, Text.translatable("item.spectrum.potion_pendant.when_worn"), false);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return super.hasGlint(stack) || PotionUtil.getCustomPotionEffects(stack).size() > 0;
	}
	
	@Override
	public int maxEffectCount() {
		return maxEffectCount;
	}
	
	@Override
	public int maxEffectAmplifier() {
		return maxAmplifier;
	}
	
	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onEquip(stack, slot, entity);
		if (!entity.getWorld().isClient && entity instanceof PlayerEntity player) {
			grantEffects(stack, player);
		}
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		if (!entity.getWorld().isClient && entity.getWorld().getTime() % TRIGGER_EVERY_X_TICKS == 0 && entity instanceof PlayerEntity player) {
			grantEffects(stack, player);
		}
	}
	
	private void grantEffects(ItemStack stack, PlayerEntity player) {
		for (InkPoweredStatusEffectInstance inkPoweredEffect : getEffects(stack)) {
			if (InkPowered.tryDrainEnergy(player, inkPoweredEffect.getInkCost())) {
				StatusEffectInstance effect = inkPoweredEffect.getStatusEffectInstance();
				player.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), EFFECT_DURATION, effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles(), true));
			}
		}
	}
	
}