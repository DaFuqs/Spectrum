package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public class FourLeafCloverItem extends CloakedBlockItem implements LoomPatternProvider {
	
	public FourLeafCloverItem(Block block, Item.Properties settings, ResourceLocation cloakAdvancementIdentifier, Item cloakItem) {
		super(block, settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public ResourceKey<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.FOUR_LEAF_CLOVER;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		addBannerPatternProviderTooltip(tooltip);
	}
	
}
