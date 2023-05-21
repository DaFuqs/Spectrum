package de.dafuqs.spectrum.items;

import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

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
