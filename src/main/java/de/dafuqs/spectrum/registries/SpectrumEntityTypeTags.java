package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.*;
import net.minecraft.registry.tag.*;
import net.minecraft.registry.*;

public class SpectrumEntityTypeTags {
	
	public static final TagKey<EntityType<?>> HERDING_STAFF_BLACKLISTED = getReference("herding_staff_blacklisted");
	
	
	private static TagKey<EntityType<?>> getReference(String id) {
		return TagKey.of(RegistryKeys.ENTITY_TYPE, SpectrumCommon.locate(id));
	}
	
}
