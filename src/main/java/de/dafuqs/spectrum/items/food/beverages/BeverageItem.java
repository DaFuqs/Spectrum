package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.DrinkItem;
import de.dafuqs.spectrum.items.food.beverages.properties.BeverageProperties;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public abstract class BeverageItem extends DrinkItem {
	
	public BeverageItem(Settings settings) {
		super(settings);
	}
	
	public abstract BeverageProperties getBeverageProperties(ItemStack itemStack);
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		getBeverageProperties(itemStack).addTooltip(itemStack, tooltip);
	}
	
}
