package de.dafuqs.spectrum.data;

import java.util.concurrent.*;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.datagen.v1.*;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;

public class SpectrumDynamicRegistryProvider extends FabricDynamicRegistryProvider {
	
	public SpectrumDynamicRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void configure(HolderLookup.Provider wrapperLookup, Entries entries) {
		entries.addAll(wrapperLookup.lookupOrThrow(Registries.ENCHANTMENT));
		entries.addAll(wrapperLookup.lookupOrThrow(SpectrumRegistryKeys.RESONANCE_PROCESSOR));
	}
	
	@Override
	public String getName() {
		return "Spectrum Registries";
	}
	
}
