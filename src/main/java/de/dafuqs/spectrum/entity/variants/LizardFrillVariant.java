package de.dafuqs.spectrum.entity.variants;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public record LizardFrillVariant(Identifier texture) {
	
	public static final LizardFrillVariant SIMPLE = register("simple", "textures/entity/lizard/frills_simple.png");
	public static final LizardFrillVariant FANCY = register("fancy", "textures/entity/lizard/frills_fancy.png");
	public static final LizardFrillVariant RUFFLED = register("ruffled", "textures/entity/lizard/frills_ruffled.png");
	public static final LizardFrillVariant MODEST = register("modest", "textures/entity/lizard/frills_modest.png");
	public static final LizardFrillVariant NONE = register("none", "textures/entity/lizard/frills_none.png");
	
	private static LizardFrillVariant register(String id, String textureId) {
		return Registry.register(SpectrumRegistries.LIZARD_FRILL_VARIANT, id, new LizardFrillVariant(SpectrumCommon.locate(textureId)));
	}
	
	public static void init() {
	}
}