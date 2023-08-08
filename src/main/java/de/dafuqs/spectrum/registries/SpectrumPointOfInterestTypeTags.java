package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.poi.*;

public class SpectrumPointOfInterestTypeTags {
	
	public static final TagKey<PointOfInterestType> LIZARD_DENS = of("lizard_dens");
	
	private static TagKey<PointOfInterestType> of(String id) {
		return TagKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, SpectrumCommon.locate(id));
	}
	
}
