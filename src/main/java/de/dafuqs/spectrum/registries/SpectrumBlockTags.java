package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.block.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;

public class SpectrumBlockTags {
	
	// PLANTS
	public static final TagKey<Block> MERMAIDS_BRUSH_PLANTABLE = getReference("mermaids_brush_plantable");
	public static final TagKey<Block> QUITOXIC_REEDS_PLANTABLE = getReference("quitoxic_reeds_plantable");
	public static final TagKey<Block> DOOM_BLOOM_PLANTABLE = getReference("doom_bloom_plantable");
	public static final TagKey<Block> ALOE_PLANTABLE = getReference("aloe_plantable");
	public static final TagKey<Block> ALOE_CONVERTED = getReference("aloe_converted");
	
	// DECAY
	public static final TagKey<Block> DECAY = getReference("decay");
	public static final TagKey<Block> DECAY_AWAY_CURABLES = getReference("decay_away_curables");
	public static final TagKey<Block> FADING_CONVERSIONS = getReference("fading_conversions");
	public static final TagKey<Block> FADING_SPECIAL_CONVERSIONS = getReference("fading_special_conversions");
	public static final TagKey<Block> FAILING_SAFE = getReference("failing_safe");
	public static final TagKey<Block> FAILING_CONVERSIONS = getReference("failing_conversions");
	public static final TagKey<Block> FAILING_SPECIAL_CONVERSIONS = getReference("failing_special_conversions");
	public static final TagKey<Block> RUIN_SAFE = getReference("ruin_safe");
	public static final TagKey<Block> FORFEITURE_SAFE = getReference("forfeiture_safe");
	public static final TagKey<Block> RUIN_BEDROCK_CONVERSIONS = getReference("decay_bedrock_conversions");
	public static final TagKey<Block> BLACK_MATERIA_CONVERSIONS = getReference("black_materia_conversions");
	
	// MISC
	public static final TagKey<Block> BLACK_SLUDGE_BLOCKS = getReference("black_sludge_blocks");
	public static final TagKey<Block> PRIMORDIAL_FIRE_BASE_BLOCKS = getReference("primordial_fire_base_blocks");
	public static final TagKey<Block> BLACKSLAG_ORE_REPLACEABLES = getReference("blackslag_ore_replaceables");
	public static final TagKey<Block> MAGICAL_LEAVES = getReference("magical_leaves");
	public static final TagKey<Block> NATURES_STAFF_TICKABLE = getReference("natures_staff_tickable");
	public static final TagKey<Block> NATURES_STAFF_STACKABLE = getReference("natures_staff_stackable");
	public static final TagKey<Block> BUILDING_STAFFS_BLACKLISTED = getReference("building_staffs_blacklisted");
	public static final TagKey<Block> CRYSTAL_APOTHECARY_HARVESTABLE = getReference("crystal_apothecary_harvestable");
	public static final TagKey<Block> GEMSTONE_BUDS = getReference("gemstone_buds");
	public static final TagKey<Block> BASE_STONE_DEEPER_DOWN = getReference("base_stone_deeper_down");
	public static final TagKey<Block> UNBREAKABLE_STRUCTURE_BLOCKS = getReference("unbreakable_structure_blocks");
	public static final TagKey<Block> RESONANCE_HARVESTABLES = getReference("resonance_harvestables");
	public static final TagKey<Block> SPAWNERS = getReference("spawners");
	
	// DEBUG
	public static final TagKey<Block> EXEMPT_FROM_MINEABLE_DEBUG_CHECK = getReference("exempt_from_mineable_debug_check");
	public static final TagKey<Block> EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK = getReference("exempt_from_loot_table_debug_check");
	
	// MEMORIES
	public static final TagKey<Block> MEMORY_NEVER_MANIFESTERS = getReference("memory_never_manifesters");
	public static final TagKey<Block> MEMORY_FAST_MANIFESTERS = getReference("memory_fast_manifesters");
	public static final TagKey<Block> MEMORY_VERY_FAST_MANIFESTERS = getReference("memory_very_fast_manifesters");
	
	
	private static TagKey<Block> getReference(String id) {
		return TagKey.of(Registry.BLOCK_KEY, SpectrumCommon.locate(id));
	}
	
}
