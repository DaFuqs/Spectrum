package de.dafuqs.spectrum.items;

import net.minecraft.block.entity.*;
import net.minecraft.registry.entry.*;

import java.util.*;

public interface LoomPatternProvider {
	
	RegistryEntry<BannerPattern> getPattern();
	
	default List<RegistryEntry<BannerPattern>> getPatterns() {
		return List.of(getPattern());
	}
	
}
