package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.items.conditional.CloakedBlockItem;
import de.dafuqs.spectrum.registries.SpectrumBannerPatterns;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FourLeafCloverItem extends CloakedBlockItem implements LoomPatternProvider {
	
	public FourLeafCloverItem(Block block, Item.Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(block, settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public LoomPattern getPattern() {
		return SpectrumBannerPatterns.FOUR_LEAF_CLOVER;
	}
	
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}
