package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;

@SuppressWarnings("unused")
public class SpectrumRegistries {

//	public static final SpectrumRegistry<FusionShrineRecipeWorldEffect> WORLD_EFFECT = register(SpectrumRegistryKeys.WORLD_EFFECT, false);
//	public static final SpectrumRegistry<GemstoneColor> GEMSTONE_COLOR = register(SpectrumRegistryKeys.GEMSTONE_COLOR, true);
//	public static final SpectrumRegistry<GlassArrowVariant> GLASS_ARROW_VARIANT = register(SpectrumRegistryKeys.GLASS_ARROW_VARIANT, true);
	public static final SpectrumRegistry<InkColor> INK_COLOR = create(SpectrumRegistryKeys.INK_COLOR, true);
//	public static final SpectrumRegistry<KindlingVariant> KINDLING_VARIANT = register(SpectrumRegistryKeys.KINDLING_VARIANT, true);
//	public static final SpectrumRegistry<LizardFrillVariant> LIZARD_FRILL_VARIANT = register(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT, true);
//	public static final SpectrumRegistry<LizardHornVariant> LIZARD_HORN_VARIANT = register(SpectrumRegistryKeys.LIZARD_HORN_VARIANT, true);
//	public static final SpectrumRegistry<PastelUpgradeSignature> PASTEL_UPGRADE = register(SpectrumRegistryKeys.PASTEL_UPGRADE, false);
//	public static final SpectrumRegistry<RecipeScaling> RECIPE_SCALING = register(SpectrumRegistryKeys.RECIPE_SCALING, true);
//
	public static final SpectrumRegistry<MapCodec<? extends ResonanceProcessor>> RESONANCE_PROCESSOR_TYPE = create(SpectrumRegistryKeys.RESONANCE_PROCESSOR_TYPE, false);
//
//	public static final SpectrumRegistry<ExplosionModifierType> EXPLOSION_MODIFIER_TYPE = register(SpectrumRegistryKeys.EXPLOSION_MODIFIER_TYPE, true);
//	public static final SpectrumRegistry<ExplosionModifier> EXPLOSION_MODIFIER = register(SpectrumRegistryKeys.EXPLOSION_MODIFIER, true);
	
	public static void registerBuiltInRegistries(NewRegistryEvent event) {
		event.register(INK_COLOR);
		event.register(RESONANCE_PROCESSOR_TYPE);
	}

	public static void registerDynamicRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(SpectrumRegistryKeys.RESONANCE_PROCESSOR, ResonanceProcessor.CODEC, ResonanceProcessor.CODEC);
	}

	private static <T> SpectrumRegistry<T> create(ResourceKey<? extends Registry<T>> key, boolean synced) {
		var registry = new SpectrumRegistry<>(key, Lifecycle.stable());
		// TODO PORT internals access...
		try {
			var method = BaseMappedRegistry.class.getDeclaredMethod("setSync", boolean.class);
			method.setAccessible(true);
			method.invoke(registry, synced);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Failed to mark registry as syncable", ex);
		}
		return registry;
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
