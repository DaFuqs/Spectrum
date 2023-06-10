package de.dafuqs.spectrum.entity.variants;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;

public record LizardHornVariant(Identifier texture) {
	
	public static final LizardHornVariant HORNY = register("horny", "textures/entity/lizard/horns_horny.png");
	public static final LizardHornVariant STRAIGHT = register("straight", "textures/entity/lizard/horns_straight.png");
	public static final LizardHornVariant FLEXIBLE = register("flexible", "textures/entity/lizard/horns_flexible.png");
	public static final LizardHornVariant QUEER = register("queer", "textures/entity/lizard/horns_queer.png");
	public static final LizardHornVariant POLY = register("poly", "textures/entity/lizard/horns_poly.png");
	public static final LizardHornVariant ONLY_LIKES_YOU_AS_A_FRIEND = register("friendzoned", "textures/entity/lizard/horns_friendzoned.png");
	
	private static LizardHornVariant register(String id, String textureId) {
		return Registry.register(SpectrumRegistries.LIZARD_HORN_VARIANT, id, new LizardHornVariant(SpectrumCommon.locate(textureId)));
	}
	
	public static void init() {
	}
}