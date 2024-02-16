package de.dafuqs.spectrum.api.item;

import net.minecraft.block.entity.*;
import net.minecraft.registry.entry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public interface LoomPatternProvider {
	
	Text PATTERN_AVAILABLE_TOOLTIP_TEXT = Text.translatable("item.spectrum.tooltip.loom_pattern_available").formatted(Formatting.GRAY);

	RegistryEntry<BannerPattern> getPattern();
	
	default List<RegistryEntry<BannerPattern>> getPatterns() {
		return List.of(getPattern());
	}
	
	default void addBannerPatternProviderTooltip(List<Text> tooltips) {
		tooltips.add(PATTERN_AVAILABLE_TOOLTIP_TEXT);
	}

}
