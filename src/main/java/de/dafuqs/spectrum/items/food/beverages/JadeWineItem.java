package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.beverages.properties.BeverageProperties;
import de.dafuqs.spectrum.items.food.beverages.properties.JadeWineBeverageProperties;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class JadeWineItem extends BeverageItem {
	
	public JadeWineItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return JadeWineBeverageProperties.getFromStack(itemStack);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		if(BeverageItem.isPreviewStack(itemStack)) {
			tooltip.add(new TranslatableText("item.spectrum.jade_wine.tooltip.preview").formatted(Formatting.GRAY));
			tooltip.add(new TranslatableText("item.spectrum.jade_wine.tooltip.preview2").formatted(Formatting.GRAY));
			tooltip.add(new TranslatableText("item.spectrum.jade_wine.tooltip.preview3").formatted(Formatting.GRAY));
		}
	}
	
}
