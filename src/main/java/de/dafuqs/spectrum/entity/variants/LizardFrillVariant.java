package de.dafuqs.spectrum.entity.variants;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public record LizardFrillVariant(Identifier texture) {
	
	public static final LizardFrillVariant SIMPLE = register("simple", "textures/entity/lizard/frills_simple.png");
	public static final LizardFrillVariant FANCY = register("fancy", "textures/entity/lizard/frills_fancy.png");
	public static final LizardFrillVariant RUFFLED = register("ruffled", "textures/entity/lizard/frills_ruffled.png");
	public static final LizardFrillVariant MODEST = register("modest", "textures/entity/lizard/frills_modest.png");
	public static final LizardFrillVariant NONE = register("none", "textures/entity/lizard/frills_none.png");
	
	public static final TagKey<LizardFrillVariant> NATURAL_VARIANT = getReference("natural");
	
	private static LizardFrillVariant register(String name, String textureId) {
		return Registry.register(SpectrumRegistries.LIZARD_FRILL_VARIANT, SpectrumCommon.locate(name), new LizardFrillVariant(SpectrumCommon.locate(textureId)));
	}
	
	private static TagKey<LizardFrillVariant> getReference(String name) {
		return TagKey.of(SpectrumRegistries.LIZARD_FRILL_VARIANT_KEY, SpectrumCommon.locate(name));
	}
	
	public static void init() {
	}
}