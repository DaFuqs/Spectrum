package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.level.block.*;

@SuppressWarnings("unused")
public class SpectrumBlockTags {
	
	// PLANTS
	public static final TagKey<Block> MERMAIDS_BRUSH_PLANTABLE = spectrum("mermaids_brush_plantable");
	public static final TagKey<Block> QUITOXIC_REEDS_PLANTABLE = spectrum("quitoxic_reeds_plantable");
	public static final TagKey<Block> QUITOXIC_REEDS_CONSUMABLE = spectrum("quitoxic_reeds_consumable");
	public static final TagKey<Block> SAWBLADE_HOLLY_PLANTABLE = spectrum("sawblade_holly_plantable");
	public static final TagKey<Block> DOOMBLOOM_PLANTABLE = spectrum("doombloom_plantable");
	public static final TagKey<Block> SNAPPING_IVY_PLANTABLE = spectrum("snapping_ivy_plantable");
	public static final TagKey<Block> ALOE_PLANTABLE = spectrum("aloe_plantable");
	public static final TagKey<Block> ALOE_CONVERTED = spectrum("aloe_converted");
	public static final TagKey<Block> GROWTH_ACCELERATORS = spectrum("growth_accelerators");
	public static final TagKey<Block> NIGHTDEW_SOILS = spectrum("nightdew_soils");
	
	// DECAY
	public static final TagKey<Block> DECAY = spectrum("decay/decay");
	public static final TagKey<Block> DECAY_AWAY_CURABLES = spectrum("decay/decay_away_curables");
	public static final TagKey<Block> DECAY_AWAY_REMOVABLES = spectrum("decay/decay_away_removables");
	public static final TagKey<Block> BLACK_MATERIA_CONVERSIONS = spectrum("decay/black_materia_conversions");
	
	public static final TagKey<Block> FADING_CONVERSIONS = spectrum("decay/fading_conversions");
	public static final TagKey<Block> FADING_SPECIAL_CONVERSIONS = spectrum("decay/fading_special_conversions");
	
	public static final TagKey<Block> FAILING_SAFE = spectrum("decay/failing_safe");
	public static final TagKey<Block> FAILING_CONVERSIONS = spectrum("decay/failing_conversions");
	public static final TagKey<Block> FAILING_SPECIAL_CONVERSIONS = spectrum("decay/failing_special_conversions");
	
	public static final TagKey<Block> RUIN_SAFE = spectrum("decay/ruin_safe");
	public static final TagKey<Block> RUIN_CONVERSIONS = spectrum("decay/ruin_conversions");
	public static final TagKey<Block> RUIN_SPECIAL_CONVERSIONS = spectrum("decay/ruin_special_conversions");
	
	public static final TagKey<Block> FORFEITURE_SAFE = spectrum("decay/forfeiture_safe");
	public static final TagKey<Block> FORFEITURE_CONVERSIONS = spectrum("decay/forfeiture_conversions");
	public static final TagKey<Block> FORFEITURE_SPECIAL_CONVERSIONS = spectrum("decay/forfeiture_special_conversions");
	
	// TECHNICAL
	public static final TagKey<Block> UNBREAKABLE_MOVABLE = spectrum("technical/unbreakable_movable");
	
	// WORLDGEN
	public static final TagKey<Block> BASE_STONE_DEEPER_DOWN = spectrum("base_stone_deeper_down");
	public static final TagKey<Block> BLACKSLAG_ORE_REPLACEABLES = spectrum("blackslag_ore_replaceables");
	public static final TagKey<Block> DEEPER_DOWN_FEATURE_REPLACEABLES = spectrum("deeper_down_feature_replaceables");
	
	// MISC
	public static final TagKey<Block> MULTITOOL_MINEABLE = spectrum("multitool_mineable");
	public static final TagKey<Block> BLACK_SLUDGE_BLOCKS = spectrum("black_sludge_blocks");
	public static final TagKey<Block> PRIMORDIAL_FIRE_BASE_BLOCKS = spectrum("primordial_fire_base_blocks");
	public static final TagKey<Block> NATURES_STAFF_TICKABLE = spectrum("natures_staff_tickable");
	public static final TagKey<Block> NATURES_STAFF_STACKABLE = spectrum("natures_staff_stackable");
	public static final TagKey<Block> NATURES_STAFF_SPREADABLE = spectrum("natures_staff_spreadable");
	public static final TagKey<Block> BUILDING_STAFFS_BLACKLISTED = spectrum("building_staffs_blacklisted");
	public static final TagKey<Block> CRYSTAL_APOTHECARY_HARVESTABLE = spectrum("crystal_apothecary_harvestable");
	public static final TagKey<Block> UNBREAKABLE_STRUCTURE_BLOCKS = spectrum("unbreakable_structure_blocks");
	public static final TagKey<Block> COLORING_BLACKLISTED = spectrum("coloring_blacklisted");
	public static final TagKey<Block> SPREADS_TO_BLACKSLAG = spectrum("spreads_to_blackslag");
	public static final TagKey<Block> OVERGROWN = spectrum("overgrown");
	public static final TagKey<Block> ASH = spectrum("ash");
	public static final TagKey<Block> PRECIPITATION_SOURCES = spectrum("precipitation_source");
	public static final TagKey<Block> ANIMALS_SPAWNABLE_ON_ADDITIONS = spectrum("animals_spawnable_on_additions");
	
	// ORES
	public static final TagKey<Block> AZURITE_ORES = spectrum("azurite_ores");
	
	// DEBUG
	public static final TagKey<Block> EXEMPT_FROM_MINEABLE_DEBUG_CHECK = spectrum("exempt_from_mineable_debug_check");
	public static final TagKey<Block> EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK = spectrum("exempt_from_loot_table_debug_check");
	
	// MEMORIES
	public static final TagKey<Block> MEMORY_NEVER_MANIFESTERS = spectrum("memory_never_manifesters");
	public static final TagKey<Block> MEMORY_FAST_MANIFESTERS = spectrum("memory_fast_manifesters");
	public static final TagKey<Block> MEMORY_VERY_FAST_MANIFESTERS = spectrum("memory_very_fast_manifesters");
	
	// CONVENTIONAL TAGS ("c" namespace)
	public static final TagKey<Block> C_LIGHTNING_RODS = conventional("lightning_rods");
	public static final TagKey<Block> C_BRUSHABLE_BLOCKS = conventional("brushable_blocks");
	public static final TagKey<Block> C_INFESTED_BLOCKS = conventional("infested_blocks");
	
	private static TagKey<Block> spectrum(String name) {
		return TagKey.create(Registries.BLOCK, SpectrumCommon.locate(name));
	}
	
	private static TagKey<Block> conventional(String name) {
		return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
	}
	
	// TODO: port datagen
	/*
	private static final DeferredRegister.Contextual<DatagenProxy.ProvidedTagBuilderBuilder<Block>> REGISTRAR = new DeferredRegistrar.Contextual<>(DatagenProxy.IS_DATAGEN);
	
	public static final TagKey<Block> C_LIGHTNING_RODS = conventional("lightning_rods", provider -> provider
			.add(Blocks.LIGHTNING_ROD)
			.addOptionalTag(ResourceLocation.parse("friendsandfoes:lightning_rods")));
	
	public static final TagKey<Block> C_BRUSHABLE_BLOCKS = conventional("brushable_blocks", provider -> provider
			.add(Blocks.SUSPICIOUS_SAND)
			.add(Blocks.SUSPICIOUS_GRAVEL)
			.addOptional(ResourceLocation.parse("the_bumblezone:pile_of_pollen_suspicious")));
	
	public static final TagKey<Block> C_INFESTED_BLOCKS = conventional("infested_blocks", provider -> provider
			.add(Blocks.INFESTED_COBBLESTONE)
			.add(Blocks.INFESTED_CHISELED_STONE_BRICKS)
			.add(Blocks.INFESTED_CRACKED_STONE_BRICKS)
			.add(Blocks.INFESTED_DEEPSLATE)
			.add(Blocks.INFESTED_STONE)
			.add(Blocks.INFESTED_MOSSY_STONE_BRICKS)
			.add(Blocks.INFESTED_STONE_BRICKS)
			.add(SpectrumBlocks.INFESTED_BLACKSLAG));
	
	private static TagKey<Block> conventional(String id, DatagenProxy.TagBuilderCallback<Block> builder) {
		TagKey<Block> tag = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", id));
		REGISTRAR.defer(ctx -> builder.build(ctx.build(tag)));
		return tag;
	}
	
	public static void provideTags(DatagenProxy.ProvidedTagBuilderBuilder<Block> provider) {
		REGISTRAR.flush(provider);
	}*/
	
}
