package de.dafuqs.spectrum.items.food;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class FreigeistItem extends DrinkItem {
	
	public FreigeistItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(new TranslatableText("item.spectrum.freigeist.tooltip"));
	}
	
}
