package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.*;

public class WorldgenHelper {
	
	public static <C extends FeatureConfig, F extends Feature<C>> F registerFeature(String name, F feature) {
		return Registry.register(Registry.FEATURE, SpectrumCommon.locate(name), feature);
	}
	
	public static <FC extends FeatureConfig, F extends Feature<FC>> RegistryEntry<ConfiguredFeature<?, ?>> registerConfiguredFeature(Identifier identifier, F feature, FC featureConfig) {
		return registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, identifier, new ConfiguredFeature<>(feature, featureConfig));
	}
	
	public static <T> RegistryEntry<T> registerConfiguredFeature(Registry<T> registry, Identifier identifier, T value) {
		return BuiltinRegistries.add(registry, identifier, value);
	}
	
}
