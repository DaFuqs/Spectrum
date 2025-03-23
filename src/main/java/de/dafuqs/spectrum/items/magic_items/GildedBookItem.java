package de.dafuqs.spectrum.items.magic_items;

import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GildedBookItem extends BookItem {
	
	public GildedBookItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
	
	@Override
	public int getEnchantability() {
		return Items.GOLDEN_PICKAXE.getEnchantability();
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.gilded_book.tooltip.enchantability").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.gilded_book.tooltip.copy_enchantments").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.gilded_book.tooltip.copy_enchantments2").formatted(Formatting.GRAY));
	}
	
}
