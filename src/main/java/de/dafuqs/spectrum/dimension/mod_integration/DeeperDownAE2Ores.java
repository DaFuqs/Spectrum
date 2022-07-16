package de.dafuqs.spectrum.dimension.mod_integration;

import net.fabricmc.loader.api.FabricLoader;

public class DeeperDownAE2Ores {
	
	public static boolean shouldRun() {
		return FabricLoader.getInstance().isModLoaded("ae2");
	}
	
	public static void register() {
		// TODO: this does not work and needs a better solution
		// if ae2 is ran after spectrum it results in air pockets (Registry.BLOCK.get() is AIR)
		/*registerAndAddOreFeature("dd_certus_quartz_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, Registry.BLOCK.get(Identifier.tryParse("ae2:quartz_ore")).getDefaultState()),
						OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, Registry.BLOCK.get(Identifier.tryParse("ae2:deepslate_quartz_ore")).getDefaultState())
				), 7),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(64), YOffset.belowTop(256)),
				CountPlacementModifier.of(256));*/
	}
	
}
