package de.dafuqs.spectrum.loot;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.compat.gofish.*;
import de.dafuqs.spectrum.entity.type_specific_predicates.*;
import de.dafuqs.spectrum.loot.conditions.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.loot.v2.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.loot.entry.*;
import net.minecraft.loot.provider.number.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumLootPoolModifiers {
	
	// TODO: Make data driven / introduce a constant for the 0.02 drops
	private static final Map<Identifier, TreasureHunterDropDefinition> treasureHunterLootPools = new HashMap<>() {{
		// Additional vanilla head drops
		put(new Identifier("entities/creeper"), new TreasureHunterDropDefinition(Items.CREEPER_HEAD, 0.02F));
		put(new Identifier("entities/skeleton"), new TreasureHunterDropDefinition(Items.SKELETON_SKULL, 0.02F));
		put(new Identifier("entities/wither_skeleton"), new TreasureHunterDropDefinition(Items.WITHER_SKELETON_SKULL, 0.1F));
		put(new Identifier("entities/zombie"), new TreasureHunterDropDefinition(Items.ZOMBIE_HEAD, 0.02F));
		put(new Identifier("entities/piglin"), new TreasureHunterDropDefinition(Items.PIGLIN_HEAD, 0.02F));
		put(new Identifier("entities/piglin_brute"), new TreasureHunterDropDefinition(Items.PIGLIN_HEAD, 0.02F));
		put(new Identifier("entities/ender_dragon"), new TreasureHunterDropDefinition(Items.DRAGON_HEAD, 0.35F)); // why not!
		
		// Spectrum head drops
		// ATTENTION: No specific enough loot tables exist for fox, axolotl, parrot and shulker variants.
		// Those are handled separately in setup()
		put(new Identifier("entities/sheep"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.SHEEP).get().asItem(), 0.02F));
		put(new Identifier("entities/bat"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.BAT).get().asItem(), 0.02F));
		put(new Identifier("entities/blaze"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.BLAZE).get().asItem(), 0.02F));
		put(new Identifier("entities/cat"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.CAT).get().asItem(), 0.02F));
		put(new Identifier("entities/cave_spider"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.CAVE_SPIDER).get().asItem(), 0.02F));
		put(new Identifier("entities/chicken"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.CHICKEN).get().asItem(), 0.02F));
		put(new Identifier("entities/cow"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.COW).get().asItem(), 0.02F));
		put(new Identifier("entities/donkey"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.DONKEY).get().asItem(), 0.02F));
		put(new Identifier("entities/drowned"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.DROWNED).get().asItem(), 0.02F));
		put(new Identifier("entities/elder_guardian"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ELDER_GUARDIAN).get().asItem(), 0.02F));
		put(new Identifier("entities/enderman"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ENDERMAN).get().asItem(), 0.02F));
		put(new Identifier("entities/endermite"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ENDERMITE).get().asItem(), 0.02F));
		put(new Identifier("entities/evoker"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.EVOKER).get().asItem(), 0.02F));
		put(new Identifier("entities/ghast"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.GHAST).get().asItem(), 0.02F));
		put(new Identifier("entities/guardian"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.GUARDIAN).get().asItem(), 0.02F));
		put(new Identifier("entities/hoglin"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.HOGLIN).get().asItem(), 0.02F));
		put(new Identifier("entities/horse"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.HORSE).get().asItem(), 0.02F));
		put(new Identifier("entities/husk"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.HUSK).get().asItem(), 0.02F));
		put(new Identifier("entities/illusioner"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ILLUSIONER).get().asItem(), 0.02F));
		put(new Identifier("entities/iron_golem"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.IRON_GOLEM).get().asItem(), 0.02F));
		put(new Identifier("entities/llama"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.LLAMA).get().asItem(), 0.02F));
		put(new Identifier("entities/magma_cube"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.MAGMA_CUBE).get().asItem(), 0.02F));
		put(new Identifier("entities/mule"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.MULE).get().asItem(), 0.02F));
		put(new Identifier("entities/ocelot"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.OCELOT).get().asItem(), 0.02F));
		put(new Identifier("entities/panda"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.PANDA).get().asItem(), 0.02F));
		put(new Identifier("entities/phantom"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.PHANTOM).get().asItem(), 0.02F));
		put(new Identifier("entities/pig"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.PIG).get().asItem(), 0.02F));
		put(new Identifier("entities/polar_bear"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.POLAR_BEAR).get().asItem(), 0.02F));
		put(new Identifier("entities/pufferfish"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.PUFFERFISH).get().asItem(), 0.02F));
		put(new Identifier("entities/rabbit"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.RABBIT).get().asItem(), 0.02F));
		put(new Identifier("entities/ravager"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.RAVAGER).get().asItem(), 0.02F));
		put(new Identifier("entities/salmon"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.SALMON).get().asItem(), 0.02F));
		put(new Identifier("entities/silverfish"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.SILVERFISH).get().asItem(), 0.02F));
		put(new Identifier("entities/slime"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.SLIME).get().asItem(), 0.02F));
		put(new Identifier("entities/snow_golem"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.SNOW_GOLEM).get().asItem(), 0.02F));
		put(new Identifier("entities/spider"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.SPIDER).get().asItem(), 0.02F));
		put(new Identifier("entities/squid"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.SQUID).get().asItem(), 0.02F));
		put(new Identifier("entities/stray"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.STRAY).get().asItem(), 0.02F));
		put(new Identifier("entities/strider"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.STRIDER).get().asItem(), 0.02F));
		put(new Identifier("entities/trader_llama"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.LLAMA).get().asItem(), 0.02F));
		put(new Identifier("entities/turtle"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.TURTLE).get().asItem(), 0.02F));
		put(new Identifier("entities/vex"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.VEX).get().asItem(), 0.02F));
		put(new Identifier("entities/villager"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.VILLAGER).get().asItem(), 0.02F));
		put(new Identifier("entities/vindicator"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.VINDICATOR).get().asItem(), 0.02F));
		put(new Identifier("entities/wandering_trader"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.WANDERING_TRADER).get().asItem(), 0.02F));
		put(new Identifier("entities/witch"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.WITCH).get().asItem(), 0.02F));
		put(new Identifier("entities/wither"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.WITHER).get().asItem(), 0.15F)); // he has 3 heads, after all!
		put(new Identifier("entities/wolf"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.WOLF).get().asItem(), 0.02F));
		put(new Identifier("entities/zoglin"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ZOGLIN).get().asItem(), 0.02F));
		put(new Identifier("entities/zombie_villager"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ZOMBIE_VILLAGER).get().asItem(), 0.02F));
		put(new Identifier("entities/zombified_piglin"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ZOMBIFIED_PIGLIN).get().asItem(), 0.02F));
		put(new Identifier("entities/bee"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.BEE).get().asItem(), 0.02F));
		put(new Identifier("entities/tropical_fish"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.TROPICAL_FISH).get().asItem(), 0.02F));
		put(new Identifier("entities/goat"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.GOAT).get().asItem(), 0.02F));
		put(new Identifier("entities/glow_squid"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.GLOW_SQUID).get().asItem(), 0.02F));
		put(new Identifier("entities/warden"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.WARDEN).get().asItem(), 0.2F));
		put(new Identifier("entities/tadpole"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.TADPOLE).get().asItem(), 0.02F));
		put(new Identifier("entities/allay"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ALLAY).get().asItem(), 0.02F));
		put(new Identifier("entities/camel"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.CAMEL).get().asItem(), 0.02F));
		put(new Identifier("entities/sniffer"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.SNIFFER).get().asItem(), 0.02F));
		put(new Identifier("entities/skeleton_horse"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.SKELETON_HORSE).get().asItem(), 0.02F));
		put(new Identifier("entities/zombie_horse"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ZOMBIE_HORSE).get().asItem(), 0.02F));
		put(new Identifier("entities/dolphin"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.DOLPHIN).get().asItem(), 0.02F));
		put(new Identifier("entities/pillager"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.PILLAGER).get().asItem(), 0.02F));
		
		put(new Identifier("spectrum:entities/egg_laying_wooly_pig"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.EGG_LAYING_WOOLY_PIG).get().asItem(), 0.1F));
		put(new Identifier("spectrum:entities/kindling"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.KINDLING).get().asItem(), 0.1F));
		put(new Identifier("spectrum:entities/preservation_turret"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.PRESERVATION_TURRET).get().asItem(), 0.1F));
		put(new Identifier("spectrum:entities/monstrosity"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.MONSTROSITY).get().asItem(), 0.1F));
		put(new Identifier("spectrum:entities/eraser"), new TreasureHunterDropDefinition(SpectrumSkullBlock.getBlock(SpectrumSkullType.ERASER).get().asItem(), 0.1F));
	}};
	
	public static void setup() {
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {

			// Treasure hunter pools
			if (treasureHunterLootPools.containsKey(id)) {
				TreasureHunterDropDefinition treasureHunterDropDefinition = treasureHunterLootPools.get(id);
				tableBuilder.pool(getLootPool(treasureHunterDropDefinition));
				// Some treasure hunter pools use custom loot conditions
				// because vanillas are too generic (fox/snow fox both use "fox" loot table)
			}
			else if (id.equals(new Identifier("archaeology/ocean_ruin_cold")) || id.equals(new Identifier("archaeology/ocean_ruin_warm")) ||
					id.equals(new Identifier("archaeology/trail_ruins_common")) || id.equals(new Identifier("archaeology/desert_pyramid")) || id.equals(new Identifier("archaeology/desert_well")))
			{
				tableBuilder.modifyPools(builder -> {
					builder.with(ItemEntry.builder(SpectrumItems.NIGHTDEW_SPROUT).weight(2).quality(-1));
				});
			}
			else if (id.equals(new Identifier("archaeology/trail_ruins_rare")))
			{
				tableBuilder.modifyPools(builder -> {
					builder.with(ItemEntry.builder(SpectrumItems.NIGHTDEW_SPROUT).weight(3).quality(-1));
				});
			}
			else if (id.equals(new Identifier("gameplay/sniffer_digging"))) {
				tableBuilder.modifyPools(builder -> {
					builder.with(ItemEntry.builder(SpectrumBlocks.WEEPING_GALA_SPRIG).weight(1));
					builder.with(ItemEntry.builder(SpectrumItems.NIGHTDEW_SPROUT).weight(2));
				});
			} else if (id.equals(new Identifier("entities/fox"))) {
				tableBuilder.pool(getFoxLootPool(FoxEntity.Type.RED, SpectrumSkullBlock.getBlock(SpectrumSkullType.FOX).get().asItem(), 0.02F));
				tableBuilder.pool(getFoxLootPool(FoxEntity.Type.SNOW, SpectrumSkullBlock.getBlock(SpectrumSkullType.FOX_ARCTIC).get().asItem(), 0.02F));
			} else if (id.equals(new Identifier("entities/mooshroom"))) {
				tableBuilder.pool(getMooshroomLootPool(MooshroomEntity.Type.BROWN, SpectrumSkullBlock.getBlock(SpectrumSkullType.MOOSHROOM_BROWN).get().asItem(), 0.02F));
				tableBuilder.pool(getMooshroomLootPool(MooshroomEntity.Type.RED, SpectrumSkullBlock.getBlock(SpectrumSkullType.MOOSHROOM_RED).get().asItem(), 0.02F));
			} else if (id.equals(new Identifier("entities/shulker"))) {
				tableBuilder.pool(getShulkerLootPool(null, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.BLACK, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_BLACK).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.BLUE, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_BLUE).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.BROWN, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_BROWN).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.CYAN, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_CYAN).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.GRAY, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_GRAY).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.GREEN, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_GREEN).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.LIGHT_BLUE, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_LIGHT_BLUE).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.LIGHT_GRAY, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_LIGHT_GRAY).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.LIME, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_LIME).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.MAGENTA, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_MAGENTA).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.ORANGE, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_ORANGE).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.PINK, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_PINK).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.PURPLE, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_PURPLE).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.RED, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_RED).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.WHITE, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_WHITE).get().asItem(), 0.05F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.YELLOW, SpectrumSkullBlock.getBlock(SpectrumSkullType.SHULKER_YELLOW).get().asItem(), 0.05F));
			} else if (id.equals(SpectrumCommon.locate("entities/lizard"))) {
				tableBuilder.pool(getLizardLootPool(InkColors.BLACK, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_BLACK).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.BLUE, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_BLUE).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.BROWN, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_BROWN).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.CYAN, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_CYAN).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.GRAY, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_GRAY).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.GREEN, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_GREEN).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.LIGHT_BLUE, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_LIGHT_BLUE).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.LIGHT_GRAY, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_LIGHT_GRAY).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.LIME, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_LIME).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.MAGENTA, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_MAGENTA).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.ORANGE, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_ORANGE).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.PINK, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_PINK).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.PURPLE, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_PURPLE).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.RED, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_RED).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.WHITE, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_WHITE).get().asItem(), 0.05F));
				tableBuilder.pool(getLizardLootPool(InkColors.YELLOW, SpectrumSkullBlock.getBlock(SpectrumSkullType.LIZARD_YELLOW).get().asItem(), 0.05F));
			} else if (id.equals(new Identifier("entities/axolotl"))) {
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.BLUE, SpectrumSkullBlock.getBlock(SpectrumSkullType.AXOLOTL_BLUE).get().asItem(), 0.02F));
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.CYAN, SpectrumSkullBlock.getBlock(SpectrumSkullType.AXOLOTL_CYAN).get().asItem(), 0.02F));
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.GOLD, SpectrumSkullBlock.getBlock(SpectrumSkullType.AXOLOTL_GOLD).get().asItem(), 0.02F));
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.LUCY, SpectrumSkullBlock.getBlock(SpectrumSkullType.AXOLOTL_LEUCISTIC).get().asItem(), 0.02F));
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.WILD, SpectrumSkullBlock.getBlock(SpectrumSkullType.AXOLOTL_WILD).get().asItem(), 0.02F));
			} else if (id.equals(new Identifier("entities/parrot"))) {
				tableBuilder.pool(getParrotLootPool(ParrotEntity.Variant.RED_BLUE, SpectrumSkullBlock.getBlock(SpectrumSkullType.PARROT_RED).get().asItem(), 0.02F));
				tableBuilder.pool(getParrotLootPool(ParrotEntity.Variant.BLUE, SpectrumSkullBlock.getBlock(SpectrumSkullType.PARROT_BLUE).get().asItem(), 0.02F));
				tableBuilder.pool(getParrotLootPool(ParrotEntity.Variant.GREEN, SpectrumSkullBlock.getBlock(SpectrumSkullType.PARROT_GREEN).get().asItem(), 0.02F));
				tableBuilder.pool(getParrotLootPool(ParrotEntity.Variant.YELLOW_BLUE, SpectrumSkullBlock.getBlock(SpectrumSkullType.PARROT_CYAN).get().asItem(), 0.02F));
				tableBuilder.pool(getParrotLootPool(ParrotEntity.Variant.GRAY, SpectrumSkullBlock.getBlock(SpectrumSkullType.PARROT_GRAY).get().asItem(), 0.02F));
			} else if (id.equals(new Identifier("entities/frog"))) {
				tableBuilder.pool(getFrogLootPool(FrogVariant.TEMPERATE, SpectrumSkullBlock.getBlock(SpectrumSkullType.FROG_TEMPERATE).get().asItem(), 0.02F));
				tableBuilder.pool(getFrogLootPool(FrogVariant.COLD, SpectrumSkullBlock.getBlock(SpectrumSkullType.FROG_COLD).get().asItem(), 0.02F));
				tableBuilder.pool(getFrogLootPool(FrogVariant.WARM, SpectrumSkullBlock.getBlock(SpectrumSkullType.FROG_WARM).get().asItem(), 0.02F));
			} else if (GoFishCompat.isLoaded()) {
				//Go-Fish compat: fishing of crates & go-fish fishies
				if (id.equals(SpectrumLootTables.LAVA_FISHING)) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.NETHER_FISH_LOOT_TABLE_ID).weight(80).quality(-1).build()));
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.NETHER_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumLootTables.END_FISHING)) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.END_FISH_LOOT_TABLE_ID).weight(90).quality(-1).build()));
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.END_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumLootTables.DEEPER_DOWN_FISHING)) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.DEFAULT_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumLootTables.MUD_FISHING)) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.DEFAULT_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumLootTables.LIQUID_CRYSTAL_FISHING)) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.DEFAULT_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumLootTables.MIDNIGHT_SOLUTION_FISHING)) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.DEFAULT_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				}
			}
		});
	}
	
	private static LootPool getLootPool(TreasureHunterDropDefinition treasureHunterDropDefinition) {
		Item dropItem = treasureHunterDropDefinition.skullItem;
		float chance = treasureHunterDropDefinition.treasureHunterMultiplier;
		
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, dropItem).build())
				.with(ItemEntry.builder(dropItem).build())
				.build();
	}
	
	private static LootPool getFoxLootPool(FoxEntity.Type foxType, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().typeSpecific(TypeSpecificPredicate.Deserializers.FOX.createPredicate(foxType)).build()).build())
				.with(ItemEntry.builder(item).build())
				.build();
	}
	
	private static LootPool getMooshroomLootPool(MooshroomEntity.Type mooshroomType, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().typeSpecific(TypeSpecificPredicate.Deserializers.MOOSHROOM.createPredicate(mooshroomType)).build()).build())
				.with(ItemEntry.builder(item).build())
				.build();
	}
	
	private static LootPool getShulkerLootPool(@Nullable DyeColor dyeColor, Item item, float chance) {
		Optional<DyeColor> c = dyeColor == null ? Optional.empty() : Optional.of(dyeColor);

		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().typeSpecific(ShulkerPredicate.of(c.orElse(null))).build()).build())
				.with(ItemEntry.builder(item).build())
				.build();
	}
	
	private static LootPool getLizardLootPool(InkColor linkColor, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().typeSpecific(LizardPredicate.of(Optional.of(linkColor), Optional.empty(), Optional.empty())).build()).build())
				.with(ItemEntry.builder(item).build())
				.build();
	}

	private static LootPool getAxolotlLootPool(AxolotlEntity.Variant variant, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().typeSpecific(TypeSpecificPredicate.Deserializers.AXOLOTL.createPredicate(variant)).build()).build())
				.with(ItemEntry.builder(item).build())
				.build();
	}

	private static LootPool getFrogLootPool(FrogVariant variant, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().typeSpecific(TypeSpecificPredicate.frog(variant)).build()).build())
				.with(ItemEntry.builder(item).build())
				.build();
	}

	private static LootPool getParrotLootPool(ParrotEntity.Variant variant, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().typeSpecific(TypeSpecificPredicate.Deserializers.PARROT.createPredicate(variant)).build()).build())
				.with(ItemEntry.builder(item).build())
				.build();
	}

	private static class TreasureHunterDropDefinition {
		public final Item skullItem;
		public final float treasureHunterMultiplier;
		
		public TreasureHunterDropDefinition(Item skullItem, float trophyHunterChance) {
			this.skullItem = skullItem;
			this.treasureHunterMultiplier = trophyHunterChance;
		}
	}
	
	
}
