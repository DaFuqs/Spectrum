package de.dafuqs.spectrum.compat.mythic_metals;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.registry.*;
import net.minecraft.world.gen.*;

import java.util.function.*;

public class MythicMetalsCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
	public void register() {
		// TODO: add in 1.19.3
		/*addOre(BiomeSelectors.tag(SpectrumBiomeTags.DD_BIOMES), "mod_integration/mythicmetals/dd_ore_adamantite");
		addOre(BiomeSelectors.includeByKey(DDDimension.DEEP_DRIPSTONE_CAVES), "mod_integration/mythicmetals/dd_ore_aquarium");
		addOre(BiomeSelectors.tag(SpectrumBiomeTags.DD_BIOMES), "mod_integration/mythicmetals/dd_ore_calcite_kyber");
		addOre(BiomeSelectors.tag(SpectrumBiomeTags.DD_BIOMES), "mod_integration/mythicmetals/dd_ore_deepslate_runite");
		addOre(BiomeSelectors.tag(SpectrumBiomeTags.DD_BIOMES), "mod_integration/mythicmetals/dd_ore_mythril");
		addOre(BiomeSelectors.tag(SpectrumBiomeTags.DD_BIOMES), "mod_integration/mythicmetals/dd_ore_orichalcum");
		addOre(BiomeSelectors.tag(SpectrumBiomeTags.DD_BIOMES), "mod_integration/mythicmetals/dd_ore_unobtainium");*/
	}
	
	private void addOre(Predicate<BiomeSelectionContext> biomeSelector, String placedFeatureName) {
		BiomeModifications.addFeature(biomeSelector, GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate(placedFeatureName)));
	}
	
	@Override
	public void registerClient() {
	
	}
	
}
