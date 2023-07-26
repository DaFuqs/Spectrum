package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.*;
import net.minecraft.registry.*;
import net.minecraft.world.gen.feature.*;

public class WorldgenHelper {
	
	// TODO - Refactor?
	
	public static <C extends FeatureConfig, F extends Feature<C>> F registerFeature(String name, F feature) {
		return Registry.register(Registries.FEATURE, SpectrumCommon.locate(name), feature);
	}
	
}
