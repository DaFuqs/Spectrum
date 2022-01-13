package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class SpectrumBlockTags {
	
	// PLANTS
	public static Tag<Block> MERMAIDS_BRUSH_PLANTABLE;
	public static Tag<Block> QUITOXIC_REEDS_PLANTABLE;

	// DECAY
	public static Tag<Block> DECAY;
	public static Tag<Block> FADING_CONVERSIONS;
	public static Tag<Block> MAGICAL_LEAVES;
	public static Tag<Block> FAILING_SAFE;
	public static Tag<Block> DECAY_OBSIDIAN_CONVERSIONS;
	public static Tag<Block> DECAY_CRYING_OBSIDIAN_CONVERSIONS;
	public static Tag<Block> RUIN_SAFE;
	public static Tag<Block> TERROR_SAFE;
	public static Tag<Block> DECAY_BEDROCK_CONVERSIONS;
	public static Tag<Block> NATURES_STAFF_TICKABLE;
	public static Tag<Block> NATURES_STAFF_STACKABLE;
	
	public static Tag<Block> BUILDING_STAFFS_BLACKLISTED;
	
	public static Tag<Block> EXEMPT_FROM_MINEABLE_DEBUG_CHECK;
	public static Tag<Block> EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK;

	private static Tag<Block> getReference(String id) {
		return TagFactory.BLOCK.create(new Identifier(SpectrumCommon.MOD_ID, id));
	}

	public static void getReferences() {
		// PLANTS
		MERMAIDS_BRUSH_PLANTABLE = getReference("mermaids_brush_plantable");
		QUITOXIC_REEDS_PLANTABLE = getReference("quitoxic_reeds_plantable");

		// DECAY
		DECAY = getReference("decay");
		FADING_CONVERSIONS = getReference("fading_conversions");
		FAILING_SAFE = getReference("failing_safe");
		MAGICAL_LEAVES = getReference("magical_leaves");
		DECAY_OBSIDIAN_CONVERSIONS = getReference("decay_obsidian_conversions");
		DECAY_CRYING_OBSIDIAN_CONVERSIONS = getReference("decay_crying_obsidian_conversions");
		RUIN_SAFE = getReference("ruin_safe");
		TERROR_SAFE = getReference("terror_safe");
		DECAY_BEDROCK_CONVERSIONS = getReference("decay_bedrock_conversions");
		NATURES_STAFF_TICKABLE = getReference("natures_staff_tickable");
		NATURES_STAFF_STACKABLE = getReference("natures_staff_stackable");
		BUILDING_STAFFS_BLACKLISTED = getReference("building_staffs_blacklisted");
		
		EXEMPT_FROM_MINEABLE_DEBUG_CHECK = getReference("exempt_from_mineable_debug_check");
		EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK = getReference("exempt_from_loot_table_debug_check");
	}
}
