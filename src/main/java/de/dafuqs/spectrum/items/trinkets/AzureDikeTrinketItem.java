package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.*;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.*;

import java.util.*;

public abstract class AzureDikeTrinketItem extends SpectrumTrinketItem implements AzureDikeItem {
	
	public AzureDikeTrinketItem(Properties settings, ResourceLocation unlockIdentifier) {
		super(settings, unlockIdentifier);
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
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.azure_dike_provider.tooltip", maxAzureDike(stack)));
	}
	
}
