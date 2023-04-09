package de.dafuqs.spectrum.loot;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.compat.gofish.*;
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

import java.util.*;

public class SpectrumLootPoolModifiers {
	
	private static final Map<Identifier, TreasureHunterDropDefinition> trophyHunterLootPools = new HashMap<>() {{
		// Additional vanilla head drops
		put(new Identifier("entities/creeper"), new TreasureHunterDropDefinition(Items.CREEPER_HEAD, 0.02F));
		put(new Identifier("entities/skeleton"), new TreasureHunterDropDefinition(Items.SKELETON_SKULL, 0.02F));
		put(new Identifier("entities/wither_skeleton"), new TreasureHunterDropDefinition(Items.WITHER_SKELETON_SKULL, 0.1F));
		put(new Identifier("entities/zombie"), new TreasureHunterDropDefinition(Items.ZOMBIE_HEAD, 0.02F));
		put(new Identifier("entities/ender_dragon"), new TreasureHunterDropDefinition(Items.DRAGON_HEAD, 0.35F)); // why not!
		
		// Spectrum head drops
		// ATTENTION: No specific enough loot tables exist for fox, axolotl, parrot and shulker variants.
		// Those are handled separately in setup()
		put(new Identifier("entities/sheep/black"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_BLACK).asItem(), 0.02F));
		put(new Identifier("entities/sheep/blue"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_BLUE).asItem(), 0.02F));
		put(new Identifier("entities/sheep/brown"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_BROWN).asItem(), 0.02F));
		put(new Identifier("entities/sheep/cyan"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_CYAN).asItem(), 0.02F));
		put(new Identifier("entities/sheep/gray"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_GRAY).asItem(), 0.02F));
		put(new Identifier("entities/sheep/green"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_GREEN).asItem(), 0.02F));
		put(new Identifier("entities/sheep/light_blue"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_LIGHT_BLUE).asItem(), 0.02F));
		put(new Identifier("entities/sheep/light_gray"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_LIGHT_GRAY).asItem(), 0.02F));
		put(new Identifier("entities/sheep/lime"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_LIME).asItem(), 0.02F));
		put(new Identifier("entities/sheep/magenta"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_MAGENTA).asItem(), 0.02F));
		put(new Identifier("entities/sheep/orange"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_ORANGE).asItem(), 0.02F));
		put(new Identifier("entities/sheep/pink"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_PINK).asItem(), 0.02F));
		put(new Identifier("entities/sheep/purple"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_PURPLE).asItem(), 0.02F));
		put(new Identifier("entities/sheep/red"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_RED).asItem(), 0.02F));
		put(new Identifier("entities/sheep/white"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_WHITE).asItem(), 0.02F));
		put(new Identifier("entities/sheep/yellow"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHEEP_YELLOW).asItem(), 0.02F));
		put(new Identifier("entities/bat"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.BAT).asItem(), 0.02F));
		put(new Identifier("entities/blaze"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.BLAZE).asItem(), 0.02F));
		put(new Identifier("entities/cat"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.CAT).asItem(), 0.02F));
		put(new Identifier("entities/cave_spider"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.CAVE_SPIDER).asItem(), 0.02F));
		put(new Identifier("entities/chicken"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.CHICKEN).asItem(), 0.02F));
		put(new Identifier("entities/cow"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.COW).asItem(), 0.02F));
		put(new Identifier("entities/donkey"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.DONKEY).asItem(), 0.02F));
		put(new Identifier("entities/drowned"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.DROWNED).asItem(), 0.02F));
		put(new Identifier("entities/elder_guardian"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.ELDER_GUARDIAN).asItem(), 0.02F));
		put(new Identifier("entities/enderman"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.ENDERMAN).asItem(), 0.02F));
		put(new Identifier("entities/endermite"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.ENDERMITE).asItem(), 0.02F));
		put(new Identifier("entities/evoker"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.EVOKER).asItem(), 0.02F));
		put(new Identifier("entities/ghast"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.GHAST).asItem(), 0.02F));
		put(new Identifier("entities/guardian"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.GUARDIAN).asItem(), 0.02F));
		put(new Identifier("entities/hoglin"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.HOGLIN).asItem(), 0.02F));
		put(new Identifier("entities/horse"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.HORSE).asItem(), 0.02F));
		put(new Identifier("entities/husk"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.HUSK).asItem(), 0.02F));
		put(new Identifier("entities/illusioner"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.ILLUSIONER).asItem(), 0.02F));
		put(new Identifier("entities/iron_golem"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.IRON_GOLEM).asItem(), 0.02F));
		put(new Identifier("entities/llama"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.LLAMA).asItem(), 0.02F));
		put(new Identifier("entities/magma_cube"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.MAGMA_CUBE).asItem(), 0.02F));
		put(new Identifier("entities/mule"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.MULE).asItem(), 0.02F));
		put(new Identifier("entities/ocelot"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.OCELOT).asItem(), 0.02F));
		put(new Identifier("entities/panda"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PANDA).asItem(), 0.02F));
		put(new Identifier("entities/phantom"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PHANTOM).asItem(), 0.02F));
		put(new Identifier("entities/pig"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PIG).asItem(), 0.02F));
		put(new Identifier("entities/piglin"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PIGLIN).asItem(), 0.02F));
		put(new Identifier("entities/polar_bear"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.POLAR_BEAR).asItem(), 0.02F));
		put(new Identifier("entities/pufferfish"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PUFFERFISH).asItem(), 0.02F));
		put(new Identifier("entities/rabbit"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.RABBIT).asItem(), 0.02F));
		put(new Identifier("entities/ravager"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.RAVAGER).asItem(), 0.02F));
		put(new Identifier("entities/salmon"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SALMON).asItem(), 0.02F));
		put(new Identifier("entities/silverfish"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SILVERFISH).asItem(), 0.02F));
		put(new Identifier("entities/slime"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SLIME).asItem(), 0.02F));
		put(new Identifier("entities/snow_golem"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SNOW_GOLEM).asItem(), 0.02F));
		put(new Identifier("entities/spider"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SPIDER).asItem(), 0.02F));
		put(new Identifier("entities/squid"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SQUID).asItem(), 0.02F));
		put(new Identifier("entities/stray"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.STRAY).asItem(), 0.02F));
		put(new Identifier("entities/strider"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.STRIDER).asItem(), 0.02F));
		put(new Identifier("entities/trader_llama"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.TRADER_LLAMA).asItem(), 0.02F));
		put(new Identifier("entities/turtle"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.TURTLE).asItem(), 0.02F));
		put(new Identifier("entities/vex"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.VEX).asItem(), 0.02F));
		put(new Identifier("entities/villager"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.VILLAGER).asItem(), 0.02F));
		put(new Identifier("entities/vindicator"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.VINDICATOR).asItem(), 0.02F));
		put(new Identifier("entities/wandering_trader"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.WANDERING_TRADER).asItem(), 0.02F));
		put(new Identifier("entities/witch"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.WITCH).asItem(), 0.02F));
		put(new Identifier("entities/wither"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.WITHER).asItem(), 0.15F)); // he has 3 heads, after all!
		put(new Identifier("entities/wolf"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.WOLF).asItem(), 0.02F));
		put(new Identifier("entities/zoglin"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.ZOGLIN).asItem(), 0.02F));
		put(new Identifier("entities/zombie_villager"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.ZOMBIE_VILLAGER).asItem(), 0.02F));
		put(new Identifier("entities/zombified_piglin"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.ZOMBIFIED_PIGLIN).asItem(), 0.02F));
		put(new Identifier("entities/bee"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.BEE).asItem(), 0.02F));
		put(new Identifier("entities/tropical_fish"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.CLOWNFISH).asItem(), 0.02F));
		put(new Identifier("entities/goat"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.GOAT).asItem(), 0.02F));
		put(new Identifier("entities/glow_squid"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.GLOW_SQUID).asItem(), 0.02F));
		put(new Identifier("entities/warden"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.WARDEN).asItem(), 0.2F));
		put(new Identifier("entities/tadpole"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.TADPOLE).asItem(), 0.02F));
		put(new Identifier("entities/allay"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.ALLAY).asItem(), 0.02F));

		put(new Identifier("spectrum:entities/egg_laying_wooly_pig"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.EGG_LAYING_WOOLY_PIG).asItem(), 0.1F));
	}};
	
	public static void setup() {
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			
			// Treasure hunter pools
			if (trophyHunterLootPools.containsKey(id)) {
				TreasureHunterDropDefinition treasureHunterDropDefinition = trophyHunterLootPools.get(id);
				tableBuilder.pool(getLootPool(treasureHunterDropDefinition));
				// Some treasure hunter pools use custom loot conditions
				// because vanillas are too generic (fox/snow fox both use "fox" loot table)
			} else if (id.equals(new Identifier("entities/fox"))) {
				tableBuilder.pool(getFoxLootPool(FoxEntity.Type.RED, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.FOX).asItem(), 0.02F));
				tableBuilder.pool(getFoxLootPool(FoxEntity.Type.SNOW, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.FOX_ARCTIC).asItem(), 0.02F));
			} else if (id.equals(new Identifier("entities/mooshroom"))) {
				tableBuilder.pool(getMooshroomLootPool(MooshroomEntity.Type.BROWN, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.MOOSHROOM_BROWN).asItem(), 0.02F));
				tableBuilder.pool(getMooshroomLootPool(MooshroomEntity.Type.RED, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.MOOSHROOM_RED).asItem(), 0.02F));
			} else if (id.equals(new Identifier("entities/shulker"))) {
				tableBuilder.pool(getShulkerLootPool(null, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.BLACK, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_BLACK).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.BLUE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_BLUE).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.BROWN, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_BROWN).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.CYAN, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_CYAN).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.GRAY, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_GRAY).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.GREEN, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_GREEN).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.LIGHT_BLUE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_LIGHT_BLUE).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.LIGHT_GRAY, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_LIGHT_GRAY).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.LIME, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_LIME).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.MAGENTA, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_MAGENTA).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.ORANGE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_ORANGE).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.PINK, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_PINK).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.PURPLE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_PURPLE).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.RED, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_RED).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.WHITE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_WHITE).asItem(), 0.5F));
				tableBuilder.pool(getShulkerLootPool(DyeColor.YELLOW, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.SHULKER_YELLOW).asItem(), 0.5F));
			} else if (id.equals(new Identifier("entities/axolotl"))) {
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.BLUE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.AXOLOTL_BLUE).asItem(), 0.02F));
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.CYAN, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.AXOLOTL_CYAN).asItem(), 0.02F));
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.GOLD, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.AXOLOTL_GOLD).asItem(), 0.02F));
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.LUCY, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.AXOLOTL_LEUCISTIC).asItem(), 0.02F));
				tableBuilder.pool(getAxolotlLootPool(AxolotlEntity.Variant.WILD, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.AXOLOTL_BROWN).asItem(), 0.02F));
			} else if (id.equals(new Identifier("entities/parrot"))) {
				tableBuilder.pool(getParrotLootPool(0, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PARROT_RED).asItem(), 0.02F));
				tableBuilder.pool(getParrotLootPool(1, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PARROT_BLUE).asItem(), 0.02F));
				tableBuilder.pool(getParrotLootPool(2, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PARROT_GREEN).asItem(), 0.02F));
				tableBuilder.pool(getParrotLootPool(3, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PARROT_CYAN).asItem(), 0.02F));
				tableBuilder.pool(getParrotLootPool(4, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PARROT_GRAY).asItem(), 0.02F));
			} else if (id.equals(new Identifier("entities/frog"))) {
				tableBuilder.pool(getFrogLootPool(FrogVariant.TEMPERATE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.FROG_TEMPERATE).asItem(), 0.02F));
				tableBuilder.pool(getFrogLootPool(FrogVariant.COLD, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.FROG_COLD).asItem(), 0.02F));
				tableBuilder.pool(getFrogLootPool(FrogVariant.WARM, SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.FROG_WARM).asItem(), 0.02F));
			} else if (GoFishCompat.isLoaded()) {
				//Go-Fish compat: fishing of crates & go-fish fishies
				if (id.equals(SpectrumCommon.locate("gameplay/fishing/lava/fishing"))) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.NETHER_FISH_LOOT_TABLE_ID).weight(80).quality(-1).build()));
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.NETHER_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumCommon.locate("gameplay/fishing/end/fishing"))) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.END_FISH_LOOT_TABLE_ID).weight(90).quality(-1).build()));
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.END_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumCommon.locate("gameplay/fishing/deeper_down/fishing"))) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.DEFAULT_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumCommon.locate("gameplay/fishing/mud/fishing"))) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.DEFAULT_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumCommon.locate("gameplay/fishing/liquid_crystal/fishing"))) {
					tableBuilder.modifyPools(builder -> builder.with(LootTableEntry.builder(GoFishCompat.DEFAULT_CRATES_LOOT_TABLE_ID).weight(5).quality(2).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, new EntityPredicate.Builder().typeSpecific(FishingHookPredicate.of(true)).build()))));
				} else if (id.equals(SpectrumCommon.locate("gameplay/fishing/midnight_solution/fishing"))) {
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
				.with(ItemEntry.builder(dropItem).build()).build();
	}
	
	private static LootPool getFoxLootPool(FoxEntity.Type foxType, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(FoxTypeLootCondition.builder(foxType).build())
				.with(ItemEntry.builder(item).build()).build();
	}
	
	private static LootPool getMooshroomLootPool(MooshroomEntity.Type mooshroomType, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(MooshroomTypeLootCondition.builder(mooshroomType).build())
				.with(ItemEntry.builder(item).build()).build();
	}
	
	private static LootPool getShulkerLootPool(DyeColor dyeColor, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(ShulkerColorLootCondition.builder(dyeColor).build())
				.with(ItemEntry.builder(item).build()).build();
	}

	private static LootPool getAxolotlLootPool(AxolotlEntity.Variant variant, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(AxolotlVariantLootCondition.builder(variant).build())
				.with(ItemEntry.builder(item).build()).build();
	}

	private static LootPool getFrogLootPool(FrogVariant variant, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(FrogVariantLootCondition.builder(variant).build())
				.with(ItemEntry.builder(item).build()).build();
	}

	private static LootPool getParrotLootPool(int variant, Item item, float chance) {
		return new LootPool.Builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.conditionally(ParrotVariantLootCondition.builder(variant).build())
				.with(ItemEntry.builder(item).build()).build();
	}

	private static class TreasureHunterDropDefinition {
		public Item skullItem;
		public float treasureHunterMultiplier;
		
		public TreasureHunterDropDefinition(Item skullItem, float trophyHunterChance) {
			this.skullItem = skullItem;
			this.treasureHunterMultiplier = trophyHunterChance;
		}
	}
	
	
}
