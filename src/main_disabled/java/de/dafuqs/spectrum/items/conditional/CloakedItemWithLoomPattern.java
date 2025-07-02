package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public class CloakedItemWithLoomPattern extends CloakedItem implements LoomPatternProvider {
	
	private final ResourceKey<BannerPattern> patternItemTag;
	
	public CloakedItemWithLoomPattern(Properties settings, ResourceLocation cloakAdvancementIdentifier, Item cloakItem, ResourceKey<BannerPattern> patternItemTag) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
		this.patternItemTag = patternItemTag;
	}
	
	@Override
	public ResourceKey<BannerPattern> getPattern() {
		return patternItemTag;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		addBannerPatternProviderTooltip(tooltip);
	}
	
}
