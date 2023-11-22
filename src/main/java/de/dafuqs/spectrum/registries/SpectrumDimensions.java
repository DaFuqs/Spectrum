package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;

public class SpectrumDimensions {
	
	public static final Identifier EFFECTS_ID = SpectrumCommon.locate("deeper_down");
	public static final Identifier DIMENSION_ID = SpectrumCommon.locate("deeper_down");
	public static final RegistryKey<World> DIMENSION_KEY = RegistryKey.of(RegistryKeys.WORLD, DIMENSION_ID);
	
	public static void register() {
	
	}
	
	public static void registerClient() {
		DimensionEffectsAccessor.getEffects().put(EFFECTS_ID, new DeeperDownDimensionEffects());
	}
	
}
