package de.dafuqs.spectrum.helpers;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

public class WorldgenHelper {
	
	public static <T extends FeatureConfig> Feature<T> registerFeature(Feature<T> feature, Identifier id) {
		return Registry.register(Registry.FEATURE, id, feature);
	}
	
	public static <FC extends FeatureConfig, F extends Feature<FC>> RegistryEntry<ConfiguredFeature<FC, ?>> registerConfiguredFeature(Identifier identifier, F feature, FC featureConfig) {
		return registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, identifier, new ConfiguredFeature<>(feature, featureConfig));
	}
	
	public static <V extends T, T> RegistryEntry<V> registerConfiguredFeature(Registry<T> registry, Identifier identifier, V value) {
		return (RegistryEntry<V>) BuiltinRegistries.add(registry, identifier, value);
	}
	
	public static RegistryEntry<PlacedFeature> registerPlacedFeature(Identifier identifier, RegistryEntry<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
		return BuiltinRegistries.add(BuiltinRegistries.PLACED_FEATURE, identifier, new PlacedFeature(RegistryEntry.upcast(feature), List.of(modifiers)));
	}
	
	public static RegistryEntry<PlacedFeature> registerConfiguredAndPlacedFeature(Identifier identifier, Feature feature, FeatureConfig featureConfig, PlacementModifier... placementModifiers) {
		RegistryEntry configuredFeature = registerConfiguredFeature(identifier, feature, featureConfig);
		return registerPlacedFeature(identifier, configuredFeature, placementModifiers);
	}
	
	public static RegistryEntry<PlacedFeature> registerConfiguredAndPlacedFeature(Identifier identifier, ConfiguredFeature configuredFeature, PlacementModifier... placementModifiers) {
		RegistryEntry id = BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
		return registerPlacedFeature(identifier, id, placementModifiers);
	}
	
}
