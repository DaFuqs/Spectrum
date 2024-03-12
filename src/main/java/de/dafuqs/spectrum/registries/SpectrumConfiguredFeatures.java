package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.dd_deco.*;
import net.minecraft.registry.*;
import net.minecraft.world.gen.feature.*;

import java.util.*;

public class SpectrumConfiguredFeatures {

	public static final RegistryKey<ConfiguredFeature<?, ?>> CLOVER_PATCH = of("clover_patch");
	public static final RegistryKey<ConfiguredFeature<?,?>> SNAPPING_IVY_PATCH = of("snapping_ivy_patch");
	public static final RegistryKey<ConfiguredFeature<?,?>> JADEITE_LOTUS = of("jadeite_lotus");
	public static final RegistryKey<ConfiguredFeature<?,?>> NEPHRITE_BLOSSOM_BULB = of("nephrite_blossom");
	public static final RegistryKey<ConfiguredFeature<?, ?>> BRISTLE_SPROUT_PATCH = of("bristle_sprouts");

	public static final RegistryKey<ConfiguredFeature<?,?>> SLATE_NOXFUNGUS = of("noxfungi/slate");
	public static final RegistryKey<ConfiguredFeature<?,?>> EBONY_NOXFUNGUS = of("noxfungi/ebony");
	public static final RegistryKey<ConfiguredFeature<?,?>> IVORY_NOXFUNGUS = of("noxfungi/ivory");
	public static final RegistryKey<ConfiguredFeature<?,?>> CHESTNUT_NOXFUNGUS = of("noxfungi/chestnut");

	public static final Map<Dragonjag.Variant, RegistryKey<ConfiguredFeature<?, ?>>> DRAGONJAGS = new HashMap<>() {{
		put(Dragonjag.Variant.PINK, of("dragonjags/pink"));
		put(Dragonjag.Variant.RED, of("dragonjags/red"));
		put(Dragonjag.Variant.BLACK, of("dragonjags/black"));
		put(Dragonjag.Variant.YELLOW, of("dragonjags/yellow"));
		put(Dragonjag.Variant.PURPLE, of("dragonjags/purple"));
	}};

	public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, SpectrumCommon.locate(id));
	}
	
}
