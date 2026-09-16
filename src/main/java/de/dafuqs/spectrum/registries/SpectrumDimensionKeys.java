package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;

public class SpectrumDimensionKeys {
	
	public static final ResourceLocation DEEPER_DOWN_ID = SpectrumCommon.locate("deeper_down");
	public static final ResourceKey<Level> DEEPER_DOWN_KEY = ResourceKey.create(Registries.DIMENSION, DEEPER_DOWN_ID);
	
	public static final ResourceLocation DEEPER_DOWN_SPECIAL_EFFECTS_ID = SpectrumCommon.locate("deeper_down");
	
	public static final ResourceLocation CONSERVATORY_ID = SpectrumCommon.locate("conservatory");
	public static final ResourceKey<Level> CONSERVATORY_KEY = ResourceKey.create(Registries.DIMENSION, CONSERVATORY_ID);
	
	public static boolean isSpectrumDimension(Level level) {
		ResourceKey<Level> dimensionKey = level.dimension();
		return dimensionKey.equals(SpectrumDimensionKeys.DEEPER_DOWN_KEY) ||  dimensionKey.equals(SpectrumDimensionKeys.CONSERVATORY_KEY);
	}
	
}
