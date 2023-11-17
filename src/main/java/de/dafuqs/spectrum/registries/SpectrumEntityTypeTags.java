package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;

public class SpectrumEntityTypeTags {
	
	public static final TagKey<EntityType<?>> STAFF_OF_REMEMBRANCE_BLACKLISTED = getReference("staff_of_remembrance_blacklisted");
	public static final TagKey<EntityType<?>> POKING_DAMAGE_IMMUNE = getReference("poking_damage_immune");
	public static final TagKey<EntityType<?>> PRIMORDIAL_FIRE_IMMUNE = getReference("primordial_fire_immune");
	public static final TagKey<EntityType<?>> CONSTRUCTS = getReference("constructs");

	
	
	private static TagKey<EntityType<?>> getReference(String id) {
		return TagKey.of(Registry.ENTITY_TYPE_KEY, SpectrumCommon.locate(id));
	}
	
}
