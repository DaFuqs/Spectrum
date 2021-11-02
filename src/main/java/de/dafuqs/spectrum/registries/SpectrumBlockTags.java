package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class SpectrumBlockTags {
	
	// PLANTS
	public static Tag<Block> MERMAIDS_BRUSH_PLANTABLE;
	public static Tag<Block> QUITOXIC_REEDS_PLANTABLE;

	// DECAY
	public static Tag<Block> DECAY;
	public static Tag<Block> MAGICAL_LEAVES;
	public static Tag<Block> FAILING_SAFE;
	public static Tag<Block> DECAY_OBSIDIAN_CONVERSIONS;
	public static Tag<Block> DECAY_CRYING_OBSIDIAN_CONVERSIONS;
	public static Tag<Block> RUIN_SAFE;
	public static Tag<Block> DECAY_BEDROCK_CONVERSIONS;
	public static Tag<Block> NATURES_STAFF_TICKABLE;
	public static Tag<Block> NATURES_STAFF_STACKABLE;
	
	public static Tag<Block> PLACEMENT_STAFF_BLACKLISTED;

	private static Tag<Block> getReference(String id) {
		return TagRegistry.block(new Identifier(SpectrumCommon.MOD_ID, id));
	}

	public static void getReferences() {
		// PLANTS
		MERMAIDS_BRUSH_PLANTABLE = getReference("mermaids_brush_plantable");
		QUITOXIC_REEDS_PLANTABLE = getReference("quitoxic_reeds_plantable");

		// DECAY
		DECAY = getReference("decay");
		FAILING_SAFE = getReference("failing_safe");
		MAGICAL_LEAVES = getReference("magical_leaves");
		DECAY_OBSIDIAN_CONVERSIONS = getReference("decay_obsidian_conversions");
		DECAY_CRYING_OBSIDIAN_CONVERSIONS = getReference("decay_crying_obsidian_conversions");
		RUIN_SAFE = getReference("ruin_safe");
		DECAY_BEDROCK_CONVERSIONS = getReference("decay_bedrock_conversions");
		NATURES_STAFF_TICKABLE = getReference("natures_staff_tickable");
		NATURES_STAFF_STACKABLE = getReference("natures_staff_stackable");
		PLACEMENT_STAFF_BLACKLISTED = getReference("placement_staff_blacklisted");
	}
}
