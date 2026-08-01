package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.pastel_network.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.pastel_network.payloads.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.state.predicate.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;

public class SpectrumRegistries {
	
	public static final Registry<FusionShrineRecipeWorldEffect> WORLD_EFFECT = register(SpectrumRegistryKeys.WORLD_EFFECT, false);
	public static final Registry<GemstoneColor> GEMSTONE_COLOR = register(SpectrumRegistryKeys.GEMSTONE_COLOR, true);
	public static final Registry<GlassArrowVariant> GLASS_ARROW_VARIANT = register(SpectrumRegistryKeys.GLASS_ARROW_VARIANT, true);
	public static final Registry<InkColor> INK_COLOR = register(SpectrumRegistryKeys.INK_COLOR, true);
	public static final Registry<PastelUpgradeSignature> PASTEL_UPGRADE = register(SpectrumRegistryKeys.PASTEL_UPGRADE, false);
	public static final Registry<MapCodec<? extends ResonanceProcessor>> RESONANCE_PROCESSOR_TYPE = register(SpectrumRegistryKeys.RESONANCE_PROCESSOR_TYPE, false);
	public static final Registry<PastelPayloadType> PASTEL_PAYLOAD_TYPE = register(SpectrumRegistryKeys.PASTEL_PAYLOAD_TYPE, false);
	public static final Registry<MapCodec<? extends PastelPayload>> PASTEL_PAYLOAD = register(SpectrumRegistryKeys.PASTEL_PAYLOAD, false);
	
	private static <T> Registry<T> register(ResourceKey<? extends Registry<T>> key, boolean synced) {
		return new RegistryBuilder<>(key).sync(synced).create();
	}
	
	public static void registerBuiltInRegistries(NewRegistryEvent event) {
		event.register(WORLD_EFFECT);
		event.register(GEMSTONE_COLOR);
		event.register(GLASS_ARROW_VARIANT);
		event.register(INK_COLOR);
		event.register(PASTEL_UPGRADE);
		event.register(RESONANCE_PROCESSOR_TYPE);
		event.register(PASTEL_PAYLOAD_TYPE);
		event.register(PASTEL_PAYLOAD);
	}

	public static void registerDynamicRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(SpectrumRegistryKeys.RESONANCE_PROCESSOR, ResonanceProcessor.CODEC, ResonanceProcessor.CODEC);
		event.dataPackRegistry(SpectrumRegistryKeys.KINDLING_VARIANT, KindlingVariant.DIRECT_CODEC, KindlingVariant.DIRECT_CODEC);
		event.dataPackRegistry(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT, LizardFrillVariant.DIRECT_CODEC, LizardFrillVariant.DIRECT_CODEC);
		event.dataPackRegistry(SpectrumRegistryKeys.LIZARD_HORN_VARIANT, LizardHornVariant.DIRECT_CODEC, LizardHornVariant.DIRECT_CODEC);
		event.dataPackRegistry(SpectrumRegistryKeys.GEODE_ORES, GeodeOreDefinition.CODEC, GeodeOreDefinition.CODEC);
	}
	
	public static <T> T getRandomTagEntry(Registry<T> registry, TagKey<T> tag, RandomSource random, T fallback) {
		Optional<HolderSet.Named<T>> tagEntries = registry.getTag(tag);
		if (tagEntries.isPresent()) {
			return tagEntries.get().get(random.nextInt(tagEntries.get().size())).value();
		} else {
			return fallback;
		}
	}
	
}
