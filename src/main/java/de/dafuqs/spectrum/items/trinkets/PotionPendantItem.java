package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.level.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class PotionPendantItem extends SpectrumCurioItem implements InkPoweredPotionFillable {
	
	private final static int TRIGGER_EVERY_X_TICKS = 300;
	private final static int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 220; // always keeps the effect active & prevents the 10 seconds of screen flashing when night vision runs out
	
	private final int maxEffectCount;
	private final int maxAmplifier;
	
	public PotionPendantItem(Properties settings, int maxEffectCount, int maxAmplifier, ResourceLocation unlockIdentifier) {
		super(settings, unlockIdentifier);
		this.maxEffectCount = maxEffectCount;
		this.maxAmplifier = maxAmplifier;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		appendPotionFillableTooltip(stack, tooltip, Component.translatable("item.spectrum.potion_pendant.when_worn"), false, context.tickRate());
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return super.isFoil(stack) || stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).hasEffects();
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
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		Level world = slotContext.entity().level();
		super.onEquip(slotContext, prevStack, stack);
		if (!world.isClientSide() && slotContext.entity() instanceof Player player) {
			grantEffects(stack, player);
		}
	}
	
	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		LivingEntity entity = slotContext.entity();
		Level world = entity.level();
		super.curioTick(slotContext, stack);
		if (!world.isClientSide() && entity.level().getGameTime() % TRIGGER_EVERY_X_TICKS == 0 && entity instanceof Player player) {
			grantEffects(stack, player);
		}
	}
	
	private void grantEffects(ItemStack stack, Player player) {
		for (InkPoweredStatusEffectInstance inkPoweredEffect : InkPoweredPotionFillable.getEffects(stack)) {
			if (InkPowered.tryDrainEnergy(player, inkPoweredEffect.getInkCost())) {
				MobEffectInstance effect = inkPoweredEffect.getStatusEffectInstance();
				player.addEffect(new MobEffectInstance(effect.getEffect(), EFFECT_DURATION, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), true));
			}
		}
	}
	
}
