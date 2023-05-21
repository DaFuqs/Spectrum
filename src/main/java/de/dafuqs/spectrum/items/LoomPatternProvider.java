package de.dafuqs.spectrum.items;

import net.minecraft.block.entity.*;
import net.minecraft.util.registry.*;

import java.util.*;

public interface LoomPatternProvider {
	
	RegistryEntry<BannerPattern> getPattern();
	
	default List<RegistryEntry<BannerPattern>> getPatterns() {
		return List.of(getPattern());
	}
	
}
