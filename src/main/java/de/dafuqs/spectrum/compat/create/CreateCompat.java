package de.dafuqs.spectrum.compat.create;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.SpectrumBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.GenerationStep;

public class CreateCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
	public void register() {
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.DD_BIOMES), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("mod_integration/create/dd_zinc_ore")));
	}
	
	@Override
	public void registerClient() {
	
	}
}
