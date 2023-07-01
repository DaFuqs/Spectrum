package de.dafuqs.spectrum.compat.create;

import de.dafuqs.spectrum.compat.*;

public class CreateCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
	public void register() {
		// TODO: add in 1.19.3
		//BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.DD_BIOMES), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("mod_integration/create/dd_zinc_ore")));
	}
	
	@Override
	public void registerClient() {
	
	}
}
