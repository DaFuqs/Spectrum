package de.dafuqs.spectrum.items;

import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumBannerPatternItem extends BannerPatternItem {
	
	public static final Text TOOLTIP_TEXT = Text.translatable("item.spectrum.tooltip.loom_pattern_available").formatted(Formatting.GRAY);
	
	private final MutableText tooltipText;
	
	public SpectrumBannerPatternItem(Settings settings, TagKey<BannerPattern> patternItemTag, String tooltipText) {
		super(patternItemTag, settings);
		this.tooltipText = Text.translatable(tooltipText);
	}
	
	public static void addBannerPatternProviderTooltip(List<Text> tooltips) {
		tooltips.add(TOOLTIP_TEXT);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(tooltipText.formatted(Formatting.GRAY));
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}
