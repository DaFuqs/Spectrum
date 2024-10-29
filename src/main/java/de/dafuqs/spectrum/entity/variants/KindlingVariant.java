package de.dafuqs.spectrum.entity.variants;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

public record KindlingVariant(Identifier defaultTexture, Identifier blinkingTexture, Identifier angryTexture,
							  Identifier clippedTexture, Identifier blinkingClippedTexture,
							  Identifier angryClippedTexture, Identifier clippingLootTable) {
	
	public static final KindlingVariant DEFAULT = register("default", "textures/entity/kindling/kindling.png", "textures/entity/kindling/kindling_blink.png", "textures/entity/kindling/kindling_angry.png", "textures/entity/kindling/kindling_clipped.png", "textures/entity/kindling/kindling_blink_clipped.png", "textures/entity/kindling/kindling_angry_clipped.png", SpectrumLootTables.KINDLING_CLIPPING);
	
	private static KindlingVariant register(String name, String defaultTexture, String blinkingTexture, String angryTexture, String clippedTexture, String blinkingClippedTexture, String angryClippedTexture, Identifier clippingLootTable) {
		return Registry.register(SpectrumRegistries.KINDLING_VARIANT, SpectrumCommon.locate(name), new KindlingVariant(SpectrumCommon.locate(defaultTexture), SpectrumCommon.locate(blinkingTexture), SpectrumCommon.locate(angryTexture), SpectrumCommon.locate(clippedTexture), SpectrumCommon.locate(blinkingClippedTexture), SpectrumCommon.locate(angryClippedTexture), clippingLootTable));
	}
	
	public static void init() {
	}
}