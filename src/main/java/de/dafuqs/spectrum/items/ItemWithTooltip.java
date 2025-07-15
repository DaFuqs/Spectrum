package de.dafuqs.spectrum.items;

import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;

import java.util.*;

public class ItemWithTooltip extends Item {
	
	private final List<MutableComponent> tooltipTexts = new ArrayList<>();
	
	public ItemWithTooltip(Properties settings, String tooltip) {
		super(settings);
		this.tooltipTexts.add(Component.translatable(tooltip));
	}
	
	public ItemWithTooltip(Properties settings, String[] tooltips) {
		super(settings);
		Arrays.stream(tooltips)
				.map(Component::translatable)
				.forEach(tooltipTexts::add);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		for (MutableComponent text : this.tooltipTexts) {
			tooltip.add(text.withStyle(ChatFormatting.GRAY));
		}
	}
}
