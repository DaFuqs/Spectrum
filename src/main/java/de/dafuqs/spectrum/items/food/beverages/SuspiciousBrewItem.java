package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.beverages.properties.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class SuspiciousBrewItem extends BeverageItem {
	
	public SuspiciousBrewItem(Settings settings) {
		super(settings);
	}
	
	@Override
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
