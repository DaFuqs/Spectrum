package de.dafuqs.spectrum.items.trinkets;

import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

import java.util.*;

public class AzureDikeCoreItem extends AzureDikeTrinketItem {
	
	public AzureDikeCoreItem(Properties settings, ResourceLocation unlockIdentifier) {
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
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.azuresque_dike_core.tooltip"));
		tooltip.add(Component.translatable("item.spectrum.azuresque_dike_core.tooltip2"));
		tooltip.add(Component.translatable("item.spectrum.azuresque_dike_core.tooltip3"));
	}
}
