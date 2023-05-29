package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.energy.storage.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AzureDikeAmuletItem extends InkDrainTrinketItem implements AzureDikeItem {
	
	public AzureDikeAmuletItem(Settings settings) {
		super(settings, UNLOCK_IDENTIFIER, InkColors.BLUE);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.spectrum.azure_dike_provider.tooltip", maxAzureDike(stack)));
		super.appendTooltip(stack, world, tooltip, context);
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
	
	@Override
	public int maxAzureDike(ItemStack stack) {
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		if (storedInk < 100) {
			return 0;
		} else {
			return getDike(storedInk);
		}
	}
	
	@Override
	public float azureDikeRechargeBonusTicks(ItemStack stack) {
		return 0;
	}
	
	@Override
	public float rechargeBonusAfterDamageTicks(ItemStack stack) {
		return 0;
	}
	
	public int getDike(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 1 + (int) (Math.log(storedInk / 100) / Math.log(8));
		}
	}
	
}