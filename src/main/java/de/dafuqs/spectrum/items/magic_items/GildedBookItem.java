package de.dafuqs.spectrum.items.magic_items;

import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;

import java.util.*;

public class GildedBookItem extends BookItem {
	
	public GildedBookItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
	
	@Override
	public int getEnchantmentValue() {
		return Items.GOLDEN_PICKAXE.getEnchantmentValue();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.gilded_book.tooltip.enchantability").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.gilded_book.tooltip.copy_enchantments").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.gilded_book.tooltip.copy_enchantments2").withStyle(ChatFormatting.GRAY));
	}
	
}
