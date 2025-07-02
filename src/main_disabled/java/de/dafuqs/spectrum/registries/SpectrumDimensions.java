package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;

public class SpectrumDimensions {
	
	public static final ResourceLocation EFFECTS_ID = SpectrumCommon.locate("deeper_down");
	public static final ResourceLocation DIMENSION_ID = SpectrumCommon.locate("deeper_down");
	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION_ID);
	
	public static void register() {
	
	}
	
	public static void registerClient() {
		DimensionEffectsAccessor.getEFFECTS().put(EFFECTS_ID, new DeeperDownDimensionEffects());
	}
	
}
