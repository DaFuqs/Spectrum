package de.dafuqs.spectrum.items.trinkets;

import net.fabricmc.api.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AzureDikeRingItem extends AzureDikeTrinketItem {
	
	public AzureDikeRingItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canEquipMoreThanOne() {
		return true;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.azure_dike_ring.tooltip"));
	}
	
	@Override
	public int maxAzureDike(ItemStack stack) {
		return 4;
	}
	
	@Override
	public float azureDikeRechargeBonusTicks(ItemStack stack) {
		return 5;
	}
	
}