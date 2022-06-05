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
	public static TagKey<Block> DECAY_AWAY_CURABLES;
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
	public static TagKey<Block> BLACK_MATERIA_CONVERSIONS;
	public static TagKey<Block> CRYSTAL_APOTHECARY_HARVESTABLE;
	public static TagKey<Block> GEMSTONE_BUDS;
	
	public static TagKey<Block> BUILDING_STAFFS_BLACKLISTED;
	
	public static TagKey<Block> EXEMPT_FROM_MINEABLE_DEBUG_CHECK;
	public static TagKey<Block> EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK;
	
	public static TagKey<Block> MEMORY_NEVER_MANIFESTERS;
	public static TagKey<Block> MEMORY_FAST_MANIFESTERS;
	public static TagKey<Block> MEMORY_VERY_FAST_MANIFESTERS;
	
	public static TagKey<Block> UNBREAKABLE_STRUCTURE_BLOCKS;
	
	private static TagKey<Block> getReference(String id) {
		return TagKey.of(Registry.BLOCK_KEY, new Identifier(SpectrumCommon.MOD_ID, id));
	}
	
	public static void getReferences() {
		// PLANTS
		MERMAIDS_BRUSH_PLANTABLE = getReference("mermaids_brush_plantable");
		QUITOXIC_REEDS_PLANTABLE = getReference("quitoxic_reeds_plantable");
		
		// DECAY
		DECAY = getReference("decay");
		DECAY_AWAY_CURABLES = getReference("decay_away_curables");
		FADING_CONVERSIONS = getReference("fading_conversions");
		FADING_SPECIAL_CONVERSIONS = getReference("fading_special_conversions");
		FAILING_SAFE = getReference("failing_safe");
		FAILING_CONVERSIONS = getReference("failing_conversions");
		FAILING_SPECIAL_CONVERSIONS = getReference("failing_special_conversions");
		RUIN_SAFE = getReference("ruin_safe");
		TERROR_SAFE = getReference("terror_safe");
		RUIN_BEDROCK_CONVERSIONS = getReference("decay_bedrock_conversions");
		BLACK_MATERIA_CONVERSIONS = getReference("black_materia_conversions");
		
		// MISC
		MAGICAL_LEAVES = getReference("magical_leaves");
		NATURES_STAFF_TICKABLE = getReference("natures_staff_tickable");
		NATURES_STAFF_STACKABLE = getReference("natures_staff_stackable");
		BUILDING_STAFFS_BLACKLISTED = getReference("building_staffs_blacklisted");
		CRYSTAL_APOTHECARY_HARVESTABLE = getReference("crystal_apothecary_harvestable");
		GEMSTONE_BUDS = getReference("gemstone_buds");
		
		// DEBUG
		EXEMPT_FROM_MINEABLE_DEBUG_CHECK = getReference("exempt_from_mineable_debug_check");
		EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK = getReference("exempt_from_loot_table_debug_check");
		
		// MEMORIES
		MEMORY_NEVER_MANIFESTERS = getReference("memory_never_manifesters");
		MEMORY_FAST_MANIFESTERS = getReference("memory_fast_manifesters");
		MEMORY_VERY_FAST_MANIFESTERS = getReference("memory_very_fast_manifesters");
		
		UNBREAKABLE_STRUCTURE_BLOCKS = getReference("unbreakable_structure_blocks");
	}
}
