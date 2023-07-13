package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.block.*;
import net.minecraft.registry.tag.*;
import net.minecraft.registry.*;

public class SpectrumBlockTags {
	
	// PLANTS
	public static final TagKey<Block> MERMAIDS_BRUSH_PLANTABLE = of("mermaids_brush_plantable");
	public static final TagKey<Block> QUITOXIC_REEDS_PLANTABLE = of("quitoxic_reeds_plantable");
	public static final TagKey<Block> SAWBLADE_HOLLY_PLANTABLE = of("sawblade_holly_plantable");
	public static final TagKey<Block> DOOMBLOOM_PLANTABLE = of("doombloom_plantable");
	public static final TagKey<Block> SNAPPING_IVY_PLANTABLE = of("snapping_ivy_plantable");
	public static final TagKey<Block> ALOE_PLANTABLE = of("aloe_plantable");
	public static final TagKey<Block> ALOE_CONVERTED = of("aloe_converted");
	
	// DECAY
	public static final TagKey<Block> DECAY = of("decay/decay");
	public static final TagKey<Block> DECAY_AWAY_CURABLES = of("decay/decay_away_curables");
	public static final TagKey<Block> BLACK_MATERIA_CONVERSIONS = of("decay/black_materia_conversions");
	
	public static final TagKey<Block> FADING_CONVERSIONS = of("decay/fading_conversions");
	public static final TagKey<Block> FADING_SPECIAL_CONVERSIONS = of("decay/fading_special_conversions");
	
	public static final TagKey<Block> FAILING_SAFE = of("decay/failing_safe");
	public static final TagKey<Block> FAILING_CONVERSIONS = of("decay/failing_conversions");
	public static final TagKey<Block> FAILING_SPECIAL_CONVERSIONS = of("decay/failing_special_conversions");
	
	public static final TagKey<Block> RUIN_SAFE = of("decay/ruin_safe");
	public static final TagKey<Block> RUIN_CONVERSIONS = of("decay/ruin_conversions");
	public static final TagKey<Block> RUIN_SPECIAL_CONVERSIONS = of("decay/ruin_special_conversions");
	
	public static final TagKey<Block> FORFEITURE_SAFE = of("decay/forfeiture_safe");
	public static final TagKey<Block> FORFEITURE_CONVERSIONS = of("decay/forfeiture_conversions");
	public static final TagKey<Block> FORFEITURE_SPECIAL_CONVERSIONS = of("decay/forfeiture_special_conversions");
	
	// MISC
	public static final TagKey<Block> BLACK_SLUDGE_BLOCKS = of("black_sludge_blocks");
	public static final TagKey<Block> PRIMORDIAL_FIRE_BASE_BLOCKS = of("primordial_fire_base_blocks");
	public static final TagKey<Block> BLACKSLAG_ORE_REPLACEABLES = of("blackslag_ore_replaceables");
	public static final TagKey<Block> NATURES_STAFF_TICKABLE = of("natures_staff_tickable");
	public static final TagKey<Block> NATURES_STAFF_STACKABLE = of("natures_staff_stackable");
	public static final TagKey<Block> BUILDING_STAFFS_BLACKLISTED = of("building_staffs_blacklisted");
	public static final TagKey<Block> CRYSTAL_APOTHECARY_HARVESTABLE = of("crystal_apothecary_harvestable");
	public static final TagKey<Block> BASE_STONE_DEEPER_DOWN = of("base_stone_deeper_down");
	public static final TagKey<Block> UNBREAKABLE_STRUCTURE_BLOCKS = of("unbreakable_structure_blocks");
	public static final TagKey<Block> RESONANCE_HARVESTABLES = of("resonance_harvestables");
	public static final TagKey<Block> SPAWNERS = of("spawners");
	public static final TagKey<Block> INK_EFFECT_BLACKLISTED = of("ink_effect_blacklisted"); // blacklisting form block coloring, repairing, ...
	public static final TagKey<Block> SPREADS_TO_BLACKSLAG = of("spreads_to_blackslag");
	
	// DEBUG
	public static final TagKey<Block> EXEMPT_FROM_MINEABLE_DEBUG_CHECK = of("exempt_from_mineable_debug_check");
	public static final TagKey<Block> EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK = of("exempt_from_loot_table_debug_check");
	
	// MEMORIES
	public static final TagKey<Block> MEMORY_NEVER_MANIFESTERS = of("memory_never_manifesters");
	public static final TagKey<Block> MEMORY_FAST_MANIFESTERS = of("memory_fast_manifesters");
	public static final TagKey<Block> MEMORY_VERY_FAST_MANIFESTERS = of("memory_very_fast_manifesters");
	
	
	private static TagKey<Block> of(String id) {
		return TagKey.of(Registries.BLOCK.getKey(), SpectrumCommon.locate(id));
	}
	
}
