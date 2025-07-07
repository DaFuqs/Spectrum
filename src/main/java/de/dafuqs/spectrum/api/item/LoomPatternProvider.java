package de.dafuqs.spectrum.api.item;

import com.google.common.collect.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public interface LoomPatternProvider {
	
	Component PATTERN_AVAILABLE_TOOLTIP_TEXT = Component.translatable("item.spectrum.tooltip.loom_pattern_available").withStyle(ChatFormatting.GRAY);
	
	ResourceKey<BannerPattern> getPattern();
	
	static ImmutableList<Holder<BannerPattern>> getPatterns(HolderGetter<BannerPattern> lookup, LoomPatternProvider provider) {
		return lookup.get(provider.getPattern()).map(pattern -> ImmutableList.of((Holder<BannerPattern>) pattern)).orElse(ImmutableList.of());
	}
	
	default void addBannerPatternProviderTooltip(List<Component> tooltips) {
		tooltips.add(PATTERN_AVAILABLE_TOOLTIP_TEXT);
	}

}
