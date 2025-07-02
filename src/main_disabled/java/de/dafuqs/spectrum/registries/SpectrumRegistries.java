package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.pastel.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.recipe.*;
import net.fabricmc.fabric.api.event.registry.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;

import java.util.*;

@SuppressWarnings("unused")
public class SpectrumRegistries {
	
	public static final SpectrumRegistry<FusionShrineRecipeWorldEffect> WORLD_EFFECT = register(SpectrumRegistryKeys.WORLD_EFFECT, false);
	public static final SpectrumRegistry<GemstoneColor> GEMSTONE_COLOR = register(SpectrumRegistryKeys.GEMSTONE_COLOR, true);
	public static final SpectrumRegistry<GlassArrowVariant> GLASS_ARROW_VARIANT = register(SpectrumRegistryKeys.GLASS_ARROW_VARIANT, true);
	public static final SpectrumRegistry<InkColor> INK_COLOR = register(SpectrumRegistryKeys.INK_COLOR, true);
	public static final SpectrumRegistry<KindlingVariant> KINDLING_VARIANT = register(SpectrumRegistryKeys.KINDLING_VARIANT, true);
	public static final SpectrumRegistry<LizardFrillVariant> LIZARD_FRILL_VARIANT = register(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT, true);
	public static final SpectrumRegistry<LizardHornVariant> LIZARD_HORN_VARIANT = register(SpectrumRegistryKeys.LIZARD_HORN_VARIANT, true);
	public static final SpectrumRegistry<PastelUpgradeSignature> PASTEL_UPGRADE = register(SpectrumRegistryKeys.PASTEL_UPGRADE, false);
	public static final SpectrumRegistry<RecipeScaling> RECIPE_SCALING = register(SpectrumRegistryKeys.RECIPE_SCALING, true);
	
	public static final SpectrumRegistry<MapCodec<? extends ResonanceProcessor>> RESONANCE_PROCESSOR_TYPE = register(SpectrumRegistryKeys.RESONANCE_PROCESSOR_TYPE, false);
	
	public static final SpectrumRegistry<ExplosionModifierType> EXPLOSION_MODIFIER_TYPE = register(SpectrumRegistryKeys.EXPLOSION_MODIFIER_TYPE, true);
	public static final SpectrumRegistry<ExplosionModifier> EXPLOSION_MODIFIER = register(SpectrumRegistryKeys.EXPLOSION_MODIFIER, true);
	
	public static void register() {
		DynamicRegistries.registerSynced(SpectrumRegistryKeys.RESONANCE_PROCESSOR, ResonanceProcessor.CODEC);
	}
	
	private static <T> SpectrumRegistry<T> register(ResourceKey<? extends Registry<T>> key, boolean synced) {
		FabricRegistryBuilder<T, SpectrumRegistry<T>> builder = FabricRegistryBuilder.from(new SpectrumRegistry<>(key, Lifecycle.stable()));
		if (synced) builder.attribute(RegistryAttribute.SYNCED);
		return builder.buildAndRegister();
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
