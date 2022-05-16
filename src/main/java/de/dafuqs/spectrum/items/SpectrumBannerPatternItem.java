package de.dafuqs.spectrum.items;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpectrumBannerPatternItem extends Item implements LoomPatternProvider {
	
	private final LoomPattern pattern;
	private final TranslatableText tooltipText;
	
	public SpectrumBannerPatternItem(Settings settings, LoomPattern pattern, String tooltipText) {
		super(settings);
		this.pattern = pattern;
		this.tooltipText = new TranslatableText(tooltipText);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(tooltipText.formatted(Formatting.GRAY));
	}
	
	@Override
	public LoomPattern getPattern() {
		return pattern;
	}
	
}
