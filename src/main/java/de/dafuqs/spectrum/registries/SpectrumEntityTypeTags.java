package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;

public class SpectrumEntityTypeTags {
	
	public static final TagKey<EntityType<?>> HERDING_STAFF_BLACKLISTED = getReference("herding_staff_blacklisted");
	public static final TagKey<EntityType<?>> POKING_DAMAGE_IMMUNE = getReference("poking_damage_immune");
	
	
	private static TagKey<EntityType<?>> getReference(String id) {
		return TagKey.of(Registry.ENTITY_TYPE_KEY, SpectrumCommon.locate(id));
	}
	
}
