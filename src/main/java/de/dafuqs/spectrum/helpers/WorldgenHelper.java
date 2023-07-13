package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

public class WorldgenHelper {

	// TODO - Refactor?

	public static <C extends FeatureConfig, F extends Feature<C>> F registerFeature(String name, F feature) {
		return Registry.register(Registries.FEATURE, SpectrumCommon.locate(name), feature);
	}
//
//	public static <FC extends FeatureConfig, F extends Feature<FC>> RegistryEntry<ConfiguredFeature<FC, ?>> registerConfiguredFeature(Identifier identifier, F feature, FC featureConfig) {
//		return registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, identifier, new ConfiguredFeature<>(feature, featureConfig));
//	}
//
//	public static <V extends T, T> RegistryEntry<V> registerConfiguredFeature(Registry<T> registry, Identifier identifier, V value) {
//		return (RegistryEntry<V>) BuiltinRegistries.add(registry, identifier, value);
//	}
//
//	public static RegistryEntry<PlacedFeature> registerPlacedFeature(Identifier identifier, RegistryEntry<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
//		return BuiltinRegistries.add(BuiltinRegistries.PLACED_FEATURE, identifier, new PlacedFeature(RegistryEntry.upcast(feature), List.of(modifiers)));
//	}
//
//	public static RegistryEntry<PlacedFeature> registerConfiguredAndPlacedFeature(Identifier identifier, Feature feature, FeatureConfig featureConfig, PlacementModifier... placementModifiers) {
//		RegistryEntry configuredFeature = registerConfiguredFeature(identifier, feature, featureConfig);
//		return registerPlacedFeature(identifier, configuredFeature, placementModifiers);
//	}
//
//	public static RegistryEntry<PlacedFeature> registerConfiguredAndPlacedFeature(Identifier identifier, ConfiguredFeature configuredFeature, PlacementModifier... placementModifiers) {
//		RegistryEntry id = BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
//		return registerPlacedFeature(identifier, id, placementModifiers);
//	}
	
}
