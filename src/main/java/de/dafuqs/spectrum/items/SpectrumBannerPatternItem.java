package de.dafuqs.spectrum.items;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
