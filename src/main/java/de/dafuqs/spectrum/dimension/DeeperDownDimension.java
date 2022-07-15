package de.dafuqs.spectrum.dimension;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class DeeperDownDimension {
	
	public static final Identifier DEEPER_DOWN_DIMENSION_ID = SpectrumCommon.locate("deeper_down_dimension");
	public static final RegistryKey<World> DEEPER_DOWN_DIMENSION_KEY = RegistryKey.of(Registry.WORLD_KEY, DEEPER_DOWN_DIMENSION_ID);
	
	public static final RegistryKey<Biome> DEEPER_DOWN_BIOME_KEY = RegistryKey.of(Registry.BIOME_KEY, SpectrumCommon.locate("deeper_down_biome"));
	public static Biome DEEPER_DOWN_BIOME;
	
	public static void setup() {
		Registry.register(BuiltinRegistries.BIOME, DEEPER_DOWN_BIOME_KEY.getValue(), DeeperDownBiome.INSTANCE);
		DEEPER_DOWN_BIOME = BuiltinRegistries.BIOME.get(SpectrumCommon.locate("deeper_down_biome"));
	}
	
}
