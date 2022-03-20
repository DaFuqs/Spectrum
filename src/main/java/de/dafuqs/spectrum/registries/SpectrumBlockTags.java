package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumBlockTags {
	
	// PLANTS
	public static TagKey<Block> MERMAIDS_BRUSH_PLANTABLE;
	public static TagKey<Block> QUITOXIC_REEDS_PLANTABLE;

	// DECAY
	public static TagKey<Block> DECAY;
	public static TagKey<Block> FADING_CONVERSIONS;
	public static TagKey<Block> FADING_SPECIAL_CONVERSIONS;
	public static TagKey<Block> MAGICAL_LEAVES;
	public static TagKey<Block> FAILING_SAFE;
	public static TagKey<Block> FAILING_CONVERSIONS;
	public static TagKey<Block> FAILING_SPECIAL_CONVERSIONS;
	public static TagKey<Block> RUIN_SAFE;
	public static TagKey<Block> TERROR_SAFE;
	public static TagKey<Block> RUIN_BEDROCK_CONVERSIONS;
	public static TagKey<Block> NATURES_STAFF_TICKABLE;
	public static TagKey<Block> NATURES_STAFF_STACKABLE;
	
	public static TagKey<Block> BUILDING_STAFFS_BLACKLISTED;
	
	public static TagKey<Block> EXEMPT_FROM_MINEABLE_DEBUG_CHECK;
	public static TagKey<Block> EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK;
	
	public static TagKey<Block> CREATURE_SPAWN_NEVER_HATCHERS;
	public static TagKey<Block> CREATURE_SPAWN_FAST_HATCHERS;
	public static TagKey<Block> CREATURE_SPAWN_VERY_FAST_HATCHERS;

	private static TagKey<Block> getReference(String id) {
		return TagKey.of(Registry.BLOCK_KEY, new Identifier(SpectrumCommon.MOD_ID, id));
	}

	public static void getReferences() {
		// PLANTS
		MERMAIDS_BRUSH_PLANTABLE = getReference("mermaids_brush_plantable");
		QUITOXIC_REEDS_PLANTABLE = getReference("quitoxic_reeds_plantable");

		// DECAY
		DECAY = getReference("decay");
		FADING_CONVERSIONS = getReference("fading_conversions");
		FADING_SPECIAL_CONVERSIONS = getReference("fading_special_conversions");
		FAILING_SAFE = getReference("failing_safe");
		FAILING_CONVERSIONS = getReference("failing_conversions");
		FAILING_SPECIAL_CONVERSIONS = getReference("failing_special_conversions");
		RUIN_SAFE = getReference("ruin_safe");
		TERROR_SAFE = getReference("terror_safe");
		RUIN_BEDROCK_CONVERSIONS = getReference("decay_bedrock_conversions");
		
		// MISC
		MAGICAL_LEAVES = getReference("magical_leaves");
		NATURES_STAFF_TICKABLE = getReference("natures_staff_tickable");
		NATURES_STAFF_STACKABLE = getReference("natures_staff_stackable");
		BUILDING_STAFFS_BLACKLISTED = getReference("building_staffs_blacklisted");
		
		// DEBUG
		EXEMPT_FROM_MINEABLE_DEBUG_CHECK = getReference("exempt_from_mineable_debug_check");
		EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK = getReference("exempt_from_loot_table_debug_check");
		
		// CREATURE SPAWN
		CREATURE_SPAWN_NEVER_HATCHERS = getReference("creature_spawn_never_hatchers");
		CREATURE_SPAWN_FAST_HATCHERS = getReference("creature_spawn_fast_hatchers");
		CREATURE_SPAWN_VERY_FAST_HATCHERS = getReference("creature_spawn_very_fast_hatchers");
	}
}
