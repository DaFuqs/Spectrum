package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.items.tools.*;
import net.fabricmc.fabric.api.event.registry.*;
import net.minecraft.registry.*;

public class SpectrumRegistries {
	public static final RegistryKey<Registry<GlassArrowVariant>> GLASS_ARROW_VARIANT_KEY = RegistryKey.ofRegistry(SpectrumCommon.locate("glass_arrow_variant"));
	public static final RegistryKey<Registry<LizardScaleVariant>>LIZARD_SCALE_VARIANT_KEY = RegistryKey.ofRegistry(SpectrumCommon.locate("lizard_scale_variant"));
	public static final RegistryKey<Registry<LizardFrillVariant>>LIZARD_FRILL_VARIANT_KEY = RegistryKey.ofRegistry(SpectrumCommon.locate("lizard_frill_variant"));
	public static final RegistryKey<Registry<LizardHornVariant>>LIZARD_HORN_VARIANT_KEY = RegistryKey.ofRegistry(SpectrumCommon.locate("lizard_horn_variant"));
	public static final Registry<GlassArrowVariant> GLASS_ARROW_VARIANT = FabricRegistryBuilder.createSimple(GLASS_ARROW_VARIANT_KEY)
			.attribute(RegistryAttribute.SYNCED)
			.buildAndRegister();
	public static final Registry<LizardScaleVariant> LIZARD_SCALE_VARIANT = FabricRegistryBuilder.createSimple(LIZARD_SCALE_VARIANT_KEY)
		.attribute(RegistryAttribute.SYNCED)
		.buildAndRegister();
	public static final Registry<LizardFrillVariant> LIZARD_FRILL_VARIANT = FabricRegistryBuilder.createSimple(LIZARD_FRILL_VARIANT_KEY)
		.attribute(RegistryAttribute.SYNCED)
		.buildAndRegister();
	public static final Registry<LizardHornVariant> LIZARD_HORN_VARIANT = FabricRegistryBuilder.createSimple(LIZARD_HORN_VARIANT_KEY)
		.attribute(RegistryAttribute.SYNCED)
		.buildAndRegister();

	public static void register() {
		GlassArrowVariant.init();
		LizardScaleVariant.init();
		LizardFrillVariant.init();
		LizardHornVariant.init();
	}

}
