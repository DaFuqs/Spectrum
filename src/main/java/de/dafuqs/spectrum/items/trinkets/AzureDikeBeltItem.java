package de.dafuqs.spectrum.items.trinkets;

import net.fabricmc.api.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AzureDikeBeltItem extends AzureDikeTrinketItem {
	
	public AzureDikeBeltItem(Settings settings) {
		super(settings);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.azure_dike_belt.tooltip"));
	}
	
	@Override
	public int maxAzureDike(ItemStack stack) {
		return 6;
	}
	
	@Override
	public float rechargeBonusAfterDamageTicks(ItemStack stack) {
		return 100;
	}
	
}