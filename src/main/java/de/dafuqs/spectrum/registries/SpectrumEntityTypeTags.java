package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;

public class SpectrumEntityTypeTags {
	
	public static final TagKey<EntityType<?>> HERDING_STAFF_BLACKLISTED = getReference("herding_staff_blacklisted");
	public static final TagKey<EntityType<?>> POKING_DAMAGE_IMMUNE = getReference("poking_damage_immune");
	
	
	private static TagKey<EntityType<?>> getReference(String id) {
		return TagKey.of(RegistryKeys.ENTITY_TYPE, SpectrumCommon.locate(id));
	}
	
}
