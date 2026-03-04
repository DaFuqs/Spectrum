package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;

public class SpectrumDimensionKeys {
	
	public static final ResourceLocation DIMENSION_ID = SpectrumCommon.locate("deeper_down");
	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION_ID);
	
	public static final ResourceLocation DIMENSION_EFFECTS_ID = SpectrumCommon.locate("deeper_down");
	
}
