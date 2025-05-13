package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.pastel.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.registry.*;

public class SpectrumRegistryKeys {
	
	public static final RegistryKey<Registry<FusionShrineRecipeWorldEffect>> WORLD_EFFECT = of("world_effect");
	public static final RegistryKey<Registry<GemstoneColor>> GEMSTONE_COLOR = of("gemstone_color");
	public static final RegistryKey<Registry<GlassArrowVariant>> GLASS_ARROW_VARIANT = of("glass_arrow_variant");
	public static final RegistryKey<Registry<InkColor>> INK_COLOR = of("ink_color");
	public static final RegistryKey<Registry<KindlingVariant>> KINDLING_VARIANT = of("kindling_variant");
	public static final RegistryKey<Registry<LizardFrillVariant>> LIZARD_FRILL_VARIANT = of("lizard_frill_variant");
	public static final RegistryKey<Registry<LizardHornVariant>> LIZARD_HORN_VARIANT = of("lizard_horn_variant");
	public static final RegistryKey<Registry<PastelUpgradeSignature>> PASTEL_UPGRADE = of("pastel_upgrade");
	public static final RegistryKey<Registry<RecipeScaling>> RECIPE_SCALING = of("recipe_scaling");
	
	public static final RegistryKey<Registry<ExplosionModifierType>> EXPLOSION_MODIFIER_TYPE = of("explosion_modifier_type");
	public static final RegistryKey<Registry<ExplosionModifier>> EXPLOSION_MODIFIER = of("explosion_effect_modifier");
	
	public static final RegistryKey<Registry<MapCodec<? extends ResonanceProcessor>>> RESONANCE_PROCESSOR_TYPE = of("resonance_processor_type");
	public static final RegistryKey<Registry<ResonanceProcessor>> RESONANCE_PROCESSOR = of("resonance_processor");
	
	private static <T> RegistryKey<Registry<T>> of(String name) {
		return RegistryKey.ofRegistry(SpectrumCommon.locate(name));
	}
	
}
