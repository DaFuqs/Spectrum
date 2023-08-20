package de.dafuqs.spectrum.entity.variants;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public record LizardScaleVariant(Identifier texture) {
	
	public static final LizardScaleVariant CYAN = register("cyan", "textures/entity/lizard/scales_cyan.png");
	public static final LizardScaleVariant MAGENTA = register("magenta", "textures/entity/lizard/scales_magenta.png");
	public static final LizardScaleVariant YELLOW = register("yellow", "textures/entity/lizard/scales_yellow.png");
	public static final LizardScaleVariant BLACK = register("black", "textures/entity/lizard/scales_black.png");
	public static final LizardScaleVariant WHITE = register("white", "textures/entity/lizard/scales_white.png");
	
	private static LizardScaleVariant register(String name, String textureId) {
		return Registry.register(SpectrumRegistries.LIZARD_SCALE_VARIANT, SpectrumCommon.locate(name), new LizardScaleVariant(SpectrumCommon.locate(textureId)));
	}
	
	public static final TagKey<LizardScaleVariant> NATURAL_VARIANT = getReference("natural");
	
	private static TagKey<LizardScaleVariant> getReference(String name) {
		return TagKey.of(SpectrumRegistries.LIZARD_SCALE_VARIANT_KEY, SpectrumCommon.locate(name));
	}
	
	public static void init() {
	}
}