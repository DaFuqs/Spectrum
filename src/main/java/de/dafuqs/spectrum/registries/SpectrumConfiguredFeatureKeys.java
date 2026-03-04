package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.deeper_down.flora.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;

public class SpectrumConfiguredFeatureKeys {
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> CLOVER_PATCH = of("clover_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SNAPPING_IVY_PATCH = of("snapping_ivy_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> JADEITE_LOTUS = of("jadeite_lotus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> NEPHRITE_BLOSSOM_BULB = of("nephrite_blossom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BRISTLE_SPROUT_PATCH = of("bristle_sprouts");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> SLATE_NOXFUNGUS = of("noxfungi/slate");
	public static final ResourceKey<ConfiguredFeature<?, ?>> EBONY_NOXFUNGUS = of("noxfungi/ebony");
	public static final ResourceKey<ConfiguredFeature<?, ?>> IVORY_NOXFUNGUS = of("noxfungi/ivory");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CHESTNUT_NOXFUNGUS = of("noxfungi/chestnut");
	
	public static final Map<Dragonjag.Variant, ResourceKey<ConfiguredFeature<?, ?>>> DRAGONJAGS = new HashMap<>() {{
		put(Dragonjag.Variant.PINK, of("dragonjags/pink"));
		put(Dragonjag.Variant.RED, of("dragonjags/red"));
		put(Dragonjag.Variant.BLACK, of("dragonjags/black"));
		put(Dragonjag.Variant.YELLOW, of("dragonjags/yellow"));
		put(Dragonjag.Variant.PURPLE, of("dragonjags/purple"));
	}};
	
	public static ResourceKey<ConfiguredFeature<?, ?>> of(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, SpectrumCommon.locate(name));
	}
	
}
