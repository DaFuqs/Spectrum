package de.dafuqs.spectrum.items.trinkets;

import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class AzureDikeTrinketItem extends SpectrumTrinketItem implements AzureDikeItem {
	
	public AzureDikeTrinketItem(Settings settings, Identifier unlockIdentifier) {
		super(settings, unlockIdentifier);
	}
	
	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onEquip(stack, slot, entity);
		recalculate(entity);
	}
	
	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onUnequip(stack, slot, entity);
		recalculate(entity);
	}
	
	@Override
	public void onBreak(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.onBreak(stack, slot, entity);
		recalculate(entity);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.azure_dike_provider.tooltip", maxAzureDike(stack)));
	}
	
	@Override
	public int maxAzureDike(ItemStack stack) {
		return 4;
	}
	
	@Override
	public float azureDikeRechargeBonusTicks(ItemStack stack) {
		return 0;
	}
	
	@Override
	public float rechargeBonusAfterDamageTicks(ItemStack stack) {
		return 0;
	}
	
}