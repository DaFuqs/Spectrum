package de.dafuqs.spectrum.items.food;

import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

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
