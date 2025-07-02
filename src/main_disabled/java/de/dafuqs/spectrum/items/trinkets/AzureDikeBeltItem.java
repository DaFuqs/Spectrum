package de.dafuqs.spectrum.items.trinkets;

import net.fabricmc.api.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

import java.util.*;

public class AzureDikeBeltItem extends AzureDikeTrinketItem {
	
	public AzureDikeBeltItem(Properties settings, ResourceLocation unlockIdentifier) {
		super(settings, unlockIdentifier);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.azure_dike_belt.tooltip"));
	}
	
	@Override
	public int maxAzureDike(ItemStack stack) {
		return 8;
	}
	
	@Override
	public float rechargeDelayAfterDamageModifier(ItemStack stack) {
		return 2.5F;
	}
	
}
