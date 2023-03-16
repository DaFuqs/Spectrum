package de.dafuqs.spectrum.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemWithTooltip extends Item {
	
	private final List<MutableText> tooltipTexts = new ArrayList<>();
	
	public ItemWithTooltip(Settings settings, String tooltip) {
		super(settings);
		this.tooltipTexts.add(Text.translatable(tooltip));
	}
	
	public ItemWithTooltip(Settings settings, String[] tooltips) {
		super(settings);
		Arrays.stream(tooltips)
				.map(Text::translatable)
				.forEach(tooltipTexts::add);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		for (MutableText text : this.tooltipTexts) {
			tooltip.add(text.formatted(Formatting.GRAY));
		}
	}
	
}
