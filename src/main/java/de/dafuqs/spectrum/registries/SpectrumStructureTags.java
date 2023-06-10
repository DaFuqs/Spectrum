package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.registry.tag.*;
import net.minecraft.registry.*;
import net.minecraft.world.gen.structure.*;

public class SpectrumStructureTags {
	
	public static final TagKey<Structure> MYSTERIOUS_COMPASS_LOCATED = of("mysterious_compass_located");
	
	private static TagKey<Structure> of(String id) {
		return TagKey.of(RegistryKeys.STRUCTURE, SpectrumCommon.locate(id));
	}
	
}
