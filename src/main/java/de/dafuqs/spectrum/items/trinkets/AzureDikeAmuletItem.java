package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.*;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.*;

import java.util.*;

public class AzureDikeAmuletItem extends InkDrainTrinketItem implements AzureDikeItem {
	
	public AzureDikeAmuletItem(Properties settings, ResourceLocation unlockIdentifier) {
		super(settings, unlockIdentifier, InkColors.BLUE);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.azure_dike_provider.tooltip", maxAzureDike(stack)));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		super.onEquip(slotContext, prevStack, stack);
		recalculate(slotContext.entity());
	}
	
	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		super.onUnequip(slotContext, newStack, stack);
		recalculate(slotContext.entity());
	}
	
	@Override
	public void curioBreak(SlotContext slotContext, ItemStack stack) {
		ICurioItem.super.curioBreak(slotContext, stack);
		
		if (slotContext.entity() instanceof ServerPlayer serverPlayerEntity) {
			SpectrumAdvancementCriteria.TRINKET_CHANGE.trigger(serverPlayerEntity);
		}
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
	
	public int getDike(long storedInk) {
		if (storedInk < 100) {
			return 0;
		} else {
			return 2 + 2 * (int) (Math.log(storedInk / 100.0f) / Math.log(8));
		}
	}
	
}
