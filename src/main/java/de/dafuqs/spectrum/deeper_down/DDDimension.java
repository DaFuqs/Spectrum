package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;

public class DDDimension {
	
	public static final Identifier DEEPER_DOWN_EFFECTS_ID = SpectrumCommon.locate("deeper_down");
	public static final Identifier DEEPER_DOWN_DIMENSION_ID = SpectrumCommon.locate("deeper_down_dimension");
	public static final RegistryKey<World> DEEPER_DOWN_DIMENSION_KEY = RegistryKey.of(Registry.WORLD_KEY, DEEPER_DOWN_DIMENSION_ID);
	
	public static void register() {
		DDPlacedFeatures.register();
	}
	
	public static void registerClient() {
		DimensionEffectsAccessor.getEffects().put(DEEPER_DOWN_EFFECTS_ID, new DeeperDownDimensionEffects());
	}
	
}
