package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.storage.loot.*;

public class SpectrumLootTables {
	
	// Shooting Stars
	public static final ResourceKey<LootTable> SHOOTING_STAR_BOUNCE = keyOf("entity/shooting_star/shooting_star_bounce");
	public static final ResourceKey<LootTable> COLORFUL_SHOOTING_STAR = keyOf("entity/shooting_star/colorful_shooting_star");
	public static final ResourceKey<LootTable> FIERY_SHOOTING_STAR = keyOf("entity/shooting_star/fiery_shooting_star");
	public static final ResourceKey<LootTable> GEMSTONE_SHOOTING_STAR = keyOf("entity/shooting_star/gemstone_shooting_star");
	public static final ResourceKey<LootTable> GLISTERING_SHOOTING_STAR = keyOf("entity/shooting_star/glistering_shooting_star");
	public static final ResourceKey<LootTable> PRISTINE_SHOOTING_STAR = keyOf("entity/shooting_star/pristine_shooting_star");
	
	// Fishing
	public static final ResourceKey<LootTable> UNIVERSAL_FISHING = keyOf("gameplay/universal_fishing");
	
	public static final ResourceKey<LootTable> LAVA_FISHING = keyOf("gameplay/fishing/lava/fishing");
	public static final ResourceKey<LootTable> END_FISHING = keyOf("gameplay/fishing/end/fishing");
	public static final ResourceKey<LootTable> DEEPER_DOWN_FISHING = keyOf("gameplay/fishing/deeper_down/fishing");
	public static final ResourceKey<LootTable> SLUDGE_FISHING = keyOf("gameplay/fishing/sludge/fishing");
	public static final ResourceKey<LootTable> LIQUID_CRYSTAL_FISHING = keyOf("gameplay/fishing/liquid_crystal/fishing");
	public static final ResourceKey<LootTable> MIDNIGHT_SOLUTION_FISHING = keyOf("gameplay/fishing/midnight_solution/fishing");
	
	// Entities
	public static final ResourceKey<LootTable> EGG_LAYING_WOOLY_PIG_SHEARING = keyOf("entities/egg_laying_wooly_pig_shearing");
	
	// Blocks
	public static final ResourceKey<LootTable> WEEPING_GALA_SPRIG_RESIN = keyOf("gameplay/weeping_gala_sprig_resin");
	public static final ResourceKey<LootTable> NIGHTDEW_VINE_RARE_DROP = keyOf("gameplay/nightdew_vine_rare_drop");
	
	public static final ResourceKey<LootTable> SAWBLADE_HOLLY_HARVESTING = keyOf("gameplay/sawblade_holly_harvesting");
	public static final ResourceKey<LootTable> SAWBLADE_HOLLY_SHEARING = keyOf("gameplay/sawblade_holly_shearing");
	
	public static final ResourceKey<LootTable> JADE_VINE_HARVESTING_PETALS = keyOf("gameplay/jade_vine_petal_harvesting");
	public static final ResourceKey<LootTable> JADE_VINE_HARVESTING_NECTAR = keyOf("gameplay/jade_vine_nectar_harvesting");
	
	public static final ResourceKey<LootTable> SLATE_NOXCAP_STRIPPING = keyOf("gameplay/stripping/slate_noxcap_stripping");
	public static final ResourceKey<LootTable> EBONY_NOXCAP_STRIPPING = keyOf("gameplay/stripping/ebony_noxcap_stripping");
	public static final ResourceKey<LootTable> IVORY_NOXCAP_STRIPPING = keyOf("gameplay/stripping/ivory_noxcap_stripping");
	public static final ResourceKey<LootTable> CHESTNUT_NOXCAP_STRIPPING = keyOf("gameplay/stripping/chestnut_noxcap_stripping");
	
	public static ResourceKey<LootTable> keyOf(String id) {
		return ResourceKey.create(Registries.LOOT_TABLE, SpectrumCommon.locate(id));
	}

}
