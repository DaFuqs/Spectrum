package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;

public class SpectrumEntityTypeTags {
	
	public static final TagKey<EntityType<?>> POKING_DAMAGE_IMMUNE = getReference("poking_damage_immune");
	public static final TagKey<EntityType<?>> PRIMORDIAL_FIRE_IMMUNE = getReference("primordial_fire_immune");
	public static final TagKey<EntityType<?>> SOULLESS = getReference("soulless");
	public static final TagKey<EntityType<?>> DRACONIC = getReference("draconic");

	public static final TagKey<EntityType<?>> SLEEP_WEAK = getReference("sleep_weak");
	public static final TagKey<EntityType<?>> SLEEP_RESISTANT = getReference("sleep_resistant");
	public static final TagKey<EntityType<?>> SLEEP_IMMUNEISH = getReference("sleep_immuneish");
	public static final TagKey<EntityType<?>> UNDEFLECTABLE = getReference("undeflectable");


	public static final TagKey<EntityType<?>> STAFF_OF_REMEMBRANCE_BLACKLISTED = getReference("staff_of_remembrance_blacklisted");
	public static final TagKey<EntityType<?>> SPAWNER_MANIPULATION_BLACKLISTED = getReference("spawner_manipulation_blacklisted");
	public static final TagKey<EntityType<?>> EVERPROMISE_RIBBON_BLACKLISTED = getReference("everpromise_ribbon_blacklisted");
	
	
	private static TagKey<EntityType<?>> getReference(String id) {
		return TagKey.of(RegistryKeys.ENTITY_TYPE, SpectrumCommon.locate(id));
	}
	
}
