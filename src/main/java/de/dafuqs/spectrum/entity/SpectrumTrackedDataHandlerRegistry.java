package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.data.*;

public class SpectrumTrackedDataHandlerRegistry {
	
	public static final TrackedDataHandler<GlassArrowVariant> GLASS_ARROW_VARIANT = TrackedDataHandler.of(SpectrumRegistries.GLASS_ARROW_VARIANT);
	
	public static final TrackedDataHandler<LizardScaleVariant> LIZARD_SCALE_VARIANT = TrackedDataHandler.of(SpectrumRegistries.LIZARD_SCALE_VARIANT);
	public static final TrackedDataHandler<LizardFrillVariant> LIZARD_FRILL_VARIANT = TrackedDataHandler.of(SpectrumRegistries.LIZARD_FRILL_VARIANT);
	public static final TrackedDataHandler<LizardHornVariant> LIZARD_HORN_VARIANT = TrackedDataHandler.of(SpectrumRegistries.LIZARD_HORN_VARIANT);
	
	public static void register() {
		TrackedDataHandlerRegistry.register(GLASS_ARROW_VARIANT);
		
		TrackedDataHandlerRegistry.register(LIZARD_SCALE_VARIANT);
		TrackedDataHandlerRegistry.register(LIZARD_FRILL_VARIANT);
		TrackedDataHandlerRegistry.register(LIZARD_HORN_VARIANT);
	}
	
}
