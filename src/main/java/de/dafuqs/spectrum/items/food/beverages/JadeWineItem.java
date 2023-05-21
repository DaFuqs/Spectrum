package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.beverages.properties.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class JadeWineItem extends BeverageItem {
	
	public JadeWineItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return JadeWineBeverageProperties.getFromStack(itemStack);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		if (BeverageItem.isPreviewStack(itemStack)) {
			tooltip.add(Text.translatable("item.spectrum.jade_wine.tooltip.preview").formatted(Formatting.GRAY));
			tooltip.add(Text.translatable("item.spectrum.jade_wine.tooltip.preview2").formatted(Formatting.GRAY));
			tooltip.add(Text.translatable("item.spectrum.jade_wine.tooltip.preview3").formatted(Formatting.GRAY));
		}
	}
	
}
