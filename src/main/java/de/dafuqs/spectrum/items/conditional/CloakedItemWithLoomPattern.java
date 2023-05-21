package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.spectrum.items.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CloakedItemWithLoomPattern extends CloakedItem implements LoomPatternProvider {
	
	private final RegistryEntry<BannerPattern> patternItemTag;
	
	public CloakedItemWithLoomPattern(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem, RegistryEntry<BannerPattern> patternItemTag) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
		this.patternItemTag = patternItemTag;
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return patternItemTag;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}
