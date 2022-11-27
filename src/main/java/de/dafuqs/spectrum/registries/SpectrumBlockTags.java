package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class SpectrumBlockTags {
	
	// PLANTS
	public static TagKey<Block> MERMAIDS_BRUSH_PLANTABLE = getReference("mermaids_brush_plantable");
	public static TagKey<Block> QUITOXIC_REEDS_PLANTABLE = getReference("quitoxic_reeds_plantable");
	public static TagKey<Block> BLACK_SLUDGE_BLOCKS = getReference("black_sludge_blocks");
	
	public static final TagKey<Block> BLACKSLAG_ORE_REPLACEABLES = getReference("blackslag_ore_replaceables");
	
	// DECAY
	public static TagKey<Block> DECAY = getReference("decay");
	public static TagKey<Block> DECAY_AWAY_CURABLES = getReference("decay_away_curables");
	public static TagKey<Block> FADING_CONVERSIONS = getReference("fading_conversions");
	public static TagKey<Block> FADING_SPECIAL_CONVERSIONS = getReference("fading_special_conversions");
	public static TagKey<Block> FAILING_SAFE = getReference("failing_safe");
	public static TagKey<Block> FAILING_CONVERSIONS = getReference("failing_conversions");
	public static TagKey<Block> FAILING_SPECIAL_CONVERSIONS = getReference("failing_special_conversions");
	public static TagKey<Block> RUIN_SAFE = getReference("ruin_safe");
	public static TagKey<Block> FORFEITURE_SAFE = getReference("forfeiture_safe");
	public static TagKey<Block> RUIN_BEDROCK_CONVERSIONS = getReference("decay_bedrock_conversions");
	public static TagKey<Block> BLACK_MATERIA_CONVERSIONS = getReference("black_materia_conversions");

	// MISC
	public static TagKey<Block> MAGICAL_LEAVES = getReference("magical_leaves");
	public static TagKey<Block> NATURES_STAFF_TICKABLE = getReference("natures_staff_tickable");
	public static TagKey<Block> NATURES_STAFF_STACKABLE = getReference("natures_staff_stackable");
	public static TagKey<Block> BUILDING_STAFFS_BLACKLISTED = getReference("building_staffs_blacklisted");
	public static TagKey<Block> CRYSTAL_APOTHECARY_HARVESTABLE = getReference("crystal_apothecary_harvestable");
	public static TagKey<Block> GEMSTONE_BUDS = getReference("gemstone_buds");
	public static final TagKey<Block> BASE_STONE_DEEPER_DOWN = getReference("base_stone_deeper_down");

	// DEBUG
	public static TagKey<Block> EXEMPT_FROM_MINEABLE_DEBUG_CHECK = getReference("exempt_from_mineable_debug_check");
	public static TagKey<Block> EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK = getReference("exempt_from_loot_table_debug_check");

	// MEMORIES
	public static TagKey<Block> MEMORY_NEVER_MANIFESTERS = getReference("memory_never_manifesters");
	public static TagKey<Block> MEMORY_FAST_MANIFESTERS = getReference("memory_fast_manifesters");
	public static TagKey<Block> MEMORY_VERY_FAST_MANIFESTERS = getReference("memory_very_fast_manifesters");

	public static TagKey<Block> UNBREAKABLE_STRUCTURE_BLOCKS = getReference("unbreakable_structure_blocks");
	public static TagKey<Block> RESONANCE_HARVESTABLES = getReference("resonance_harvestables");
	public static TagKey<Block> SPAWNERS = getReference("spawners");
	
	private static TagKey<Block> getReference(String id) {
		return TagKey.of(Registry.BLOCK_KEY, SpectrumCommon.locate(id));
	}
	
}
