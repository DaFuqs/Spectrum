package de.dafuqs.spectrum.items.food;

import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class RestorationTeaItem extends TeaItem {
	
	public RestorationTeaItem(Settings settings, FoodComponent bonusFoodComponentWithScone) {
		super(settings, bonusFoodComponentWithScone);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		tooltip.add(Text.translatable("item.spectrum.restoration_tea.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.restoration_tea.tooltip2").formatted(Formatting.GRAY));
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
	}
	
}
