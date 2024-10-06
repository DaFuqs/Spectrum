package de.dafuqs.spectrum.items.trinkets;

import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AzureDikeCoreItem extends AzureDikeTrinketItem {

	public AzureDikeCoreItem(Settings settings) {
		super(settings);
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
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.spectrum.azuresque_dike_core.tooltip"));
		tooltip.add(Text.translatable("item.spectrum.azuresque_dike_core.tooltip2"));
		tooltip.add(Text.translatable("item.spectrum.azuresque_dike_core.tooltip3"));
	}
}
