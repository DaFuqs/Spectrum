package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.structure.*;

public class SpectrumStructureTags {
	
	public static final TagKey<Structure> MYSTERIOUS_COMPASS_LOCATED = getReference("mysterious_compass_located");
	
	private static TagKey<Structure> getReference(String id) {
		return TagKey.of(Registry.STRUCTURE_KEY, SpectrumCommon.locate(id));
	}
	
}
