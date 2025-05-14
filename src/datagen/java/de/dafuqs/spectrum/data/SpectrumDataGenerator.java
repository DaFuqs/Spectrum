package de.dafuqs.spectrum.data;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.datagen.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import org.jetbrains.annotations.*;

public class SpectrumDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public @Nullable String getEffectiveModId() {
		return SpectrumCommon.MOD_ID;
	}
	
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(SpectrumItemTagProvider::new);
		pack.addProvider(SpectrumBlockTagProvider::new);
		pack.addProvider(SpectrumEnchantmentTagProvider::new);
		pack.addProvider(SpectrumDynamicRegistryProvider::new);
		pack.addProvider(SpectrumModelProvider::new);
		pack.addProvider(SpectrumRecipeProvider::new);
	}
	
	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.ENCHANTMENT, registerable -> SpectrumEnchantments.provideEnchantments(new DatagenProxy.BootstrapContext<>(registerable)));
		registryBuilder.add(SpectrumRegistryKeys.RESONANCE_PROCESSOR, registerable -> SpectrumResonanceProcessors.provideResonanceProcessors(new DatagenProxy.BootstrapContext<>(registerable)));
	}
	
}
