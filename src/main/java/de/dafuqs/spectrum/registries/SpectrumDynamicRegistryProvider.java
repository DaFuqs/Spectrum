package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

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
