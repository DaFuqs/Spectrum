package de.dafuqs.spectrum.items;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.util.registry.RegistryEntry;

import java.util.List;

public interface LoomPatternProvider {
	
	RegistryEntry<BannerPattern> getPattern();
	
	default List<RegistryEntry<BannerPattern>> getPatterns() {
		return List.of(getPattern());
	}
	
}
