package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.datagen.v1.*;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.registry.*;

import java.util.concurrent.*;

public class SpectrumDynamicRegistryProvider extends FabricDynamicRegistryProvider {
	
	public SpectrumDynamicRegistryProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
		for (var key : SpectrumWorldgen.PLACED_FEATURE_KEYS.values()) {
			entries.add(key, registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE).getOrThrow(key).value());
		}
		for (var key : SpectrumWorldgen.CONFIGURED_FEATURE_KEYS.values()) {
			entries.add(key, registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE).getOrThrow(key).value());
		}
	}
	
	@Override
	public String getName() {
		return "Spectrum dynamic registries";
	}
}
