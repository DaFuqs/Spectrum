package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.pastel_network.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.pastel_network.payloads.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.items.tools.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;

public class SpectrumRegistryKeys {
	
	public static final ResourceKey<Registry<WorldEffect>> WORLD_EFFECT = of("world_effect");
	public static final ResourceKey<Registry<GemstoneColor>> GEMSTONE_COLOR = of("gemstone_color");
	public static final ResourceKey<Registry<GlassArrowVariant>> GLASS_ARROW_VARIANT = of("glass_arrow_variant");
	public static final ResourceKey<Registry<InkColor>> INK_COLOR = of("ink_color");
	public static final ResourceKey<Registry<KindlingVariant>> KINDLING_VARIANT = of("kindling_variant");
	public static final ResourceKey<Registry<LizardFrillVariant>> LIZARD_FRILL_VARIANT = of("lizard_frill_variant");
	public static final ResourceKey<Registry<LizardHornVariant>> LIZARD_HORN_VARIANT = of("lizard_horn_variant");
	public static final ResourceKey<Registry<PastelUpgradeSignature>> PASTEL_UPGRADE = of("pastel_upgrade");
	public static final ResourceKey<Registry<MapCodec<? extends ResonanceProcessor>>> RESONANCE_PROCESSOR_TYPE = of("resonance_processor_type");
	public static final ResourceKey<Registry<ResonanceProcessor>> RESONANCE_PROCESSOR = of("resonance_processor");
	public static final ResourceKey<Registry<GeodeOreDefinition>> GEODE_ORES = of("geode_ores");
	public static final ResourceKey<Registry<PastelPayloadType>> PASTEL_PAYLOAD_TYPE = of("pastel_payload_type");
	public static final ResourceKey<Registry<MapCodec<? extends PastelPayload>>> PASTEL_PAYLOAD = of("pastel_payload");
	
	private static <T> ResourceKey<Registry<T>> of(String name) {
		return ResourceKey.createRegistryKey(SpectrumCommon.locate(name));
	}
	
}
