package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.level.*;

public class SpectrumDimensionTags {
	
	public static final TagKey<Level> RUIN_SAFE = of("ruin_safe");
	
	private static TagKey<Level> of(String id) {
		return TagKey.create(Registries.DIMENSION, SpectrumCommon.locate(id));
	}
	
	public static boolean is(Level level, TagKey<Level> tag) {
		Registry<Level> registry = level.registryAccess().registryOrThrow(Registries.DIMENSION);
		ResourceKey<Level> key = level.dimension();
		Holder.Reference<Level> resourceKey = registry.getHolderOrThrow(key);
		return registry.getTag(tag).get().contains(resourceKey);
	}
}
