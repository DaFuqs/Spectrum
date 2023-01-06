package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;

public class DDDimension {
	
	public static final Identifier DEEPER_DOWN_EFFECTS_ID = SpectrumCommon.locate("deeper_down");
	public static final Identifier DEEPER_DOWN_DIMENSION_ID = SpectrumCommon.locate("deeper_down_dimension");
	public static final RegistryKey<World> DEEPER_DOWN_DIMENSION_KEY = RegistryKey.of(Registry.WORLD_KEY, DEEPER_DOWN_DIMENSION_ID);
	
	public static final RegistryKey<Biome> DEEPER_DOWN_BIOME_KEY = RegistryKey.of(Registry.BIOME_KEY, SpectrumCommon.locate("deeper_down_biome"));
	public static Biome DEEPER_DOWN_BIOME;

	public static void register() {
		Registry.register(BuiltinRegistries.BIOME, DEEPER_DOWN_BIOME_KEY.getValue(), DDBiome.INSTANCE);
		DEEPER_DOWN_BIOME = BuiltinRegistries.BIOME.get(SpectrumCommon.locate("deeper_down_biome"));

		DimensionEffectsAccessor.getEffects().put(DEEPER_DOWN_EFFECTS_ID, new DeeperDownDimensionEffects());

		DDPlacedFeatures.register();
	}
	
}
