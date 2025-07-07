package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.ai.village.poi.*;

public class SpectrumPointOfInterestTypeTags {
	
	public static final TagKey<PoiType> LIZARD_DENS = of("lizard_dens");
	
	private static TagKey<PoiType> of(String id) {
		return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, SpectrumCommon.locate(id));
	}
	
}
