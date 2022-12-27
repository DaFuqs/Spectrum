package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.items.tools.GlassArrowItem;
import de.dafuqs.spectrum.registries.SpectrumRegistries;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class SpectrumTrackedDataHandlerRegistry {
	
	public static final TrackedDataHandler<GlassArrowItem.Variant> GLASS_ARROW_VARIANT = TrackedDataHandler.of(SpectrumRegistries.GLASS_ARROW_VARIANT);
	
	public static void register() {
		TrackedDataHandlerRegistry.register(GLASS_ARROW_VARIANT);
	}
	
}
