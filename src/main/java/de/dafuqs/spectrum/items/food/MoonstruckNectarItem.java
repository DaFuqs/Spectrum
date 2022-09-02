package de.dafuqs.spectrum.items.food;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class MoonstruckNectarItem extends DrinkItem {
	
	public MoonstruckNectarItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(Text.translatable("item.spectrum.moonstruck_nectar.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.moonstruck_nectar.tooltip2").formatted(Formatting.GRAY));
	}
	
}
