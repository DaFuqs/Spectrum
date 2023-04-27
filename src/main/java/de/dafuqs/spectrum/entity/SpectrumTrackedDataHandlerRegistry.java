package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.data.*;

public class SpectrumTrackedDataHandlerRegistry {
	
	public static final TrackedDataHandler<GlassArrowVariant> GLASS_ARROW_VARIANT = TrackedDataHandler.of(SpectrumRegistries.GLASS_ARROW_VARIANT);
	
	public static void register() {
		TrackedDataHandlerRegistry.register(GLASS_ARROW_VARIANT);
	}
	
}
