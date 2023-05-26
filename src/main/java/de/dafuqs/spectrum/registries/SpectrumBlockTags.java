package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.block.*;
import net.minecraft.registry.tag.*;
import net.minecraft.registry.*;

public class SpectrumBlockTags {
	
	// PLANTS
	public static final TagKey<Block> MERMAIDS_BRUSH_PLANTABLE = getReference("mermaids_brush_plantable");
	public static final TagKey<Block> QUITOXIC_REEDS_PLANTABLE = getReference("quitoxic_reeds_plantable");
	public static final TagKey<Block> SAWBLADE_HOLLY_PLANTABLE = getReference("sawblade_holly_plantable");
	public static final TagKey<Block> DOOMBLOOM_PLANTABLE = getReference("doombloom_plantable");
	public static final TagKey<Block> SNAPPING_IVY_PLANTABLE = getReference("snapping_ivy_plantable");
	public static final TagKey<Block> ALOE_PLANTABLE = getReference("aloe_plantable");
	public static final TagKey<Block> ALOE_CONVERTED = getReference("aloe_converted");
	
	// DECAY
	public static final TagKey<Block> DECAY = getReference("decay/decay");
	public static final TagKey<Block> DECAY_AWAY_CURABLES = getReference("decay/decay_away_curables");
	public static final TagKey<Block> BLACK_MATERIA_CONVERSIONS = getReference("decay/black_materia_conversions");
	
	public static final TagKey<Block> FADING_CONVERSIONS = getReference("decay/fading_conversions");
	public static final TagKey<Block> FADING_SPECIAL_CONVERSIONS = getReference("decay/fading_special_conversions");
	
	public static final TagKey<Block> FAILING_SAFE = getReference("decay/failing_safe");
	public static final TagKey<Block> FAILING_CONVERSIONS = getReference("decay/failing_conversions");
	public static final TagKey<Block> FAILING_SPECIAL_CONVERSIONS = getReference("decay/failing_special_conversions");
	
	public static final TagKey<Block> RUIN_SAFE = getReference("decay/ruin_safe");
	public static final TagKey<Block> RUIN_CONVERSIONS = getReference("decay/ruin_conversions");
	public static final TagKey<Block> RUIN_SPECIAL_CONVERSIONS = getReference("decay/ruin_special_conversions");
	
	public static final TagKey<Block> FORFEITURE_SAFE = getReference("decay/forfeiture_safe");
	public static final TagKey<Block> FORFEITURE_CONVERSIONS = getReference("decay/forfeiture_conversions");
	public static final TagKey<Block> FORFEITURE_SPECIAL_CONVERSIONS = getReference("decay/forfeiture_special_conversions");
	
	// MISC
	public static final TagKey<Block> BLACK_SLUDGE_BLOCKS = getReference("black_sludge_blocks");
	public static final TagKey<Block> PRIMORDIAL_FIRE_BASE_BLOCKS = getReference("primordial_fire_base_blocks");
	public static final TagKey<Block> BLACKSLAG_ORE_REPLACEABLES = getReference("blackslag_ore_replaceables");
	public static final TagKey<Block> NATURES_STAFF_TICKABLE = getReference("natures_staff_tickable");
	public static final TagKey<Block> NATURES_STAFF_STACKABLE = getReference("natures_staff_stackable");
	public static final TagKey<Block> BUILDING_STAFFS_BLACKLISTED = getReference("building_staffs_blacklisted");
	public static final TagKey<Block> CRYSTAL_APOTHECARY_HARVESTABLE = getReference("crystal_apothecary_harvestable");
	public static final TagKey<Block> BASE_STONE_DEEPER_DOWN = getReference("base_stone_deeper_down");
	public static final TagKey<Block> UNBREAKABLE_STRUCTURE_BLOCKS = getReference("unbreakable_structure_blocks");
	public static final TagKey<Block> RESONANCE_HARVESTABLES = getReference("resonance_harvestables");
	public static final TagKey<Block> SPAWNERS = getReference("spawners");
	public static final TagKey<Block> CRUMBLING_SUPER_EFFECTIVE = getReference("crumbling_super_effective");
	public static final TagKey<Block> INK_EFFECT_BLACKLISTED = getReference("ink_effect_blacklisted"); // blacklisting form block coloring, repairing, ...
	public static final TagKey<Block> SPREADS_TO_BLACKSLAG = getReference("spreads_to_blackslag");
	
	// DEBUG
	public static final TagKey<Block> EXEMPT_FROM_MINEABLE_DEBUG_CHECK = getReference("exempt_from_mineable_debug_check");
	public static final TagKey<Block> EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK = getReference("exempt_from_loot_table_debug_check");
	
	// MEMORIES
	public static final TagKey<Block> MEMORY_NEVER_MANIFESTERS = getReference("memory_never_manifesters");
	public static final TagKey<Block> MEMORY_FAST_MANIFESTERS = getReference("memory_fast_manifesters");
	public static final TagKey<Block> MEMORY_VERY_FAST_MANIFESTERS = getReference("memory_very_fast_manifesters");
	
	
	private static TagKey<Block> getReference(String id) {
		return TagKey.of(Registries.BLOCK.getKey(), SpectrumCommon.locate(id));
	}
	
}
