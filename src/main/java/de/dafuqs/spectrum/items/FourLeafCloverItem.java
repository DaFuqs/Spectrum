package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.items.conditional.CloakedBlockItem;
import de.dafuqs.spectrum.registries.SpectrumBannerPatterns;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class FourLeafCloverItem extends CloakedBlockItem implements LoomPatternProvider {
	
	public FourLeafCloverItem(Block block, Item.Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(block, settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public LoomPattern getPattern() {
		return SpectrumBannerPatterns.FOUR_LEAF_CLOVER;
	}
	
}
