package de.dafuqs.spectrum.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemWithTooltip extends Item {
	
	private final List<TranslatableText> tooltipTexts = new ArrayList<>();
	
	public ItemWithTooltip(Settings settings, String tooltip) {
		super(settings);
		this.tooltipTexts.add(new TranslatableText(tooltip));
	}
	
	public ItemWithTooltip(Settings settings, String[] tooltips) {
		super(settings);
		for(String tooltip : tooltips) {
			this.tooltipTexts.add(new TranslatableText(tooltip));
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		for(TranslatableText text : this.tooltipTexts) {
			tooltip.add(text.formatted(Formatting.GRAY));
		}
	}
	
}
