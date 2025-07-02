package de.dafuqs.spectrum.items.food;

import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class AliasedTooltipItem extends ItemNameBlockItem {
	
	private final List<MutableComponent> tooltipTexts = new ArrayList<>();
	
	public AliasedTooltipItem(Block block, Properties settings, String tooltip) {
		super(block, settings);
		this.tooltipTexts.add(Component.translatable(tooltip));
	}
	
	public AliasedTooltipItem(Block block, Properties settings, String[] tooltips) {
		super(block, settings);
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
