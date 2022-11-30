package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.beverages.properties.BeverageProperties;
import de.dafuqs.spectrum.items.food.beverages.properties.StatusEffectBeverageProperties;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class SuspiciousBrewItem extends BeverageItem {
	
	public SuspiciousBrewItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return StatusEffectBeverageProperties.getFromStack(itemStack);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		if (BeverageItem.isPreviewStack(itemStack)) {
			tooltip.add(Text.translatable("item.spectrum.suspicious_brew.tooltip.preview").formatted(Formatting.GRAY));
			tooltip.add(Text.translatable("item.spectrum.suspicious_brew.tooltip.preview2").formatted(Formatting.GRAY));
		}
	}
	
}
