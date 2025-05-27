package de.dafuqs.spectrum.items.trinkets;

import net.minecraft.item.*;
import net.minecraft.item.tooltip.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class AzureDikeCoreItem extends AzureDikeTrinketItem {
	
	public AzureDikeCoreItem(Settings settings, Identifier unlockIdentifier) {
		super(settings, unlockIdentifier);
	}

	@Override
	public int maxAzureDike(ItemStack stack) {
		return 0;
	}

	@Override
	public float maxAzureDikeMultiplier(ItemStack stack) {
		return 2F;
	}

	@Override
	public float rechargeDelayAfterDamageModifier(ItemStack stack) {
		return 1.5F;
	}

	@Override
	public float azureDikeRechargeSpeedModifier(ItemStack stack) {
		return 1.5F;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("item.spectrum.azuresque_dike_core.tooltip"));
		tooltip.add(Text.translatable("item.spectrum.azuresque_dike_core.tooltip2"));
		tooltip.add(Text.translatable("item.spectrum.azuresque_dike_core.tooltip3"));
	}
}
