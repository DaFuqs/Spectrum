package de.dafuqs.spectrum.entity.variants;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public record KindlingVariant(Identifier defaultTexture, Identifier blinkingTexture, Identifier angryTexture,
							  Identifier clippedTexture, Identifier blinkingClippedTexture,
							  Identifier angryClippedTexture) {

	public static final KindlingVariant DEFAULT = register("default", "textures/entity/kindling/kindling.png", "textures/entity/kindling/kindling_blink.png", "textures/entity/kindling/kindling_angry.png", "textures/entity/kindling/kindling_clipped.png", "textures/entity/kindling/kindling_blink_clipped.png", "textures/entity/kindling/kindling_angry_clipped.png");

	private static KindlingVariant register(String name, String defaultTexture, String blinkingTexture, String angryTexture, String clippedTexture, String blinkingClippedTexture, String angryClippedTexture) {
		return Registry.register(SpectrumRegistries.KINDLING_VARIANT, SpectrumCommon.locate(name), new KindlingVariant(SpectrumCommon.locate(defaultTexture), SpectrumCommon.locate(blinkingTexture), SpectrumCommon.locate(angryTexture), SpectrumCommon.locate(clippedTexture), SpectrumCommon.locate(blinkingClippedTexture), SpectrumCommon.locate(angryClippedTexture)));
	}

	public static void init() {
	}
}