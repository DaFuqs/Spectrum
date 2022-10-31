package de.dafuqs.spectrum.items.food;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class RestorationTeaItem extends TeaItem {
	
	public RestorationTeaItem(Settings settings, FoodComponent bonusFoodComponentWithScone) {
		super(settings, bonusFoodComponentWithScone);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		tooltip.add(new TranslatableText("item.spectrum.restoration_tea.tooltip").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.restoration_tea.tooltip2").formatted(Formatting.GRAY));
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
	}
	
}
