package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;

public class DDDimension {
	
	public static final Identifier EFFECTS_ID = SpectrumCommon.locate("deeper_down");
	public static final Identifier DIMENSION_ID = SpectrumCommon.locate("deeper_down");
	public static final RegistryKey<World> DIMENSION_KEY = RegistryKey.of(RegistryKeys.WORLD, DIMENSION_ID);
	
	public static final RegistryKey<Biome> CRYSTAL_GARDENS = getBiomeKey("crystal_gardens");
	public static final RegistryKey<Biome> DEEP_BARRENS = getBiomeKey("deep_barrens");
	public static final RegistryKey<Biome> DRAGONROT_SWAMP = getBiomeKey("dragonrot_swamp");
	public static final RegistryKey<Biome> BLACK_LANGAST = getBiomeKey("black_langast");
	public static final RegistryKey<Biome> RAZOR_EDGE = getBiomeKey("razor_edge");
	
	private static RegistryKey<Biome> getBiomeKey(String name) {
		return RegistryKey.of(RegistryKeys.BIOME, SpectrumCommon.locate(name));
	}
	
	public static void register() {
	
	}
	
	public static void registerClient() {
		DimensionEffectsAccessor.getEffects().put(EFFECTS_ID, new DeeperDownDimensionEffects());
	}
	
}
