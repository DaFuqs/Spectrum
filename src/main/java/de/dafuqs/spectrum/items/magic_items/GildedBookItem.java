package de.dafuqs.spectrum.items.magic_items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GildedBookItem extends BookItem {
	
	public GildedBookItem(Settings settings) {
		super(settings);
	}
	
	public int getEnchantability() {
		return Items.GOLDEN_PICKAXE.getEnchantability();
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.gilded_book.tooltip.enchantability").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.gilded_book.tooltip.copy_enchantments").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.gilded_book.tooltip.copy_enchantments2").formatted(Formatting.GRAY));
	}
	
}
