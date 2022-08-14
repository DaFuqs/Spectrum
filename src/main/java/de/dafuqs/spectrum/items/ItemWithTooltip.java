package de.dafuqs.spectrum.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemWithTooltip extends Item {
	
	MutableText tooltipText;
	
	public ItemWithTooltip(Settings settings, String tooltip) {
		super(settings);
		this.tooltipText = Text.translatable(tooltip);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(tooltipText.formatted(Formatting.GRAY));
	}
}
