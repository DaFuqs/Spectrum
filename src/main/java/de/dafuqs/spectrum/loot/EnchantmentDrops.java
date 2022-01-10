package de.dafuqs.spectrum.loot;

import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlock;
import de.dafuqs.spectrum.loot.conditions.*;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.PlayerPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentDrops {

	private static class TreasureHunterDropDefinition {
		public Item skullItem;
		public float treasureHunterMultiplier;

		public TreasureHunterDropDefinition(Item skullItem, float trophyHunterChance) {
			this.skullItem = skullItem;
			this.treasureHunterMultiplier = trophyHunterChance;
		}
	}

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
		put(new Identifier("entities/sheep/black"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_BLACK).asItem(), 0.01F));
		put(new Identifier("entities/sheep/blue"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_BLUE).asItem(), 0.01F));
		put(new Identifier("entities/sheep/brown"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_BROWN).asItem(), 0.01F));
		put(new Identifier("entities/sheep/cyan"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_CYAN).asItem(), 0.01F));
		put(new Identifier("entities/sheep/gray"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_GRAY).asItem(), 0.01F));
		put(new Identifier("entities/sheep/green"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_GREEN).asItem(), 0.01F));
		put(new Identifier("entities/sheep/light_blue"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_LIGHT_BLUE).asItem(), 0.01F));
		put(new Identifier("entities/sheep/light_gray"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_LIGHT_GRAY).asItem(), 0.01F));
		put(new Identifier("entities/sheep/lime"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_LIME).asItem(), 0.01F));
		put(new Identifier("entities/sheep/magenta"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_MAGENTA).asItem(), 0.01F));
		put(new Identifier("entities/sheep/orange"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_ORANGE).asItem(), 0.01F));
		put(new Identifier("entities/sheep/pink"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_PINK).asItem(), 0.01F));
		put(new Identifier("entities/sheep/purple"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_PURPLE).asItem(), 0.01F));
		put(new Identifier("entities/sheep/red"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_RED).asItem(), 0.01F));
		put(new Identifier("entities/sheep/white"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_WHITE).asItem(), 0.01F));
		put(new Identifier("entities/sheep/yellow"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHEEP_YELLOW).asItem(), 0.01F));
		put(new Identifier("entities/bat"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.BAT).asItem(), 0.01F));
		put(new Identifier("entities/blaze"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.BLAZE).asItem(), 0.01F));
		put(new Identifier("entities/cat"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.CAT).asItem(), 0.01F));
		put(new Identifier("entities/cave_spider"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.CAVE_SPIDER).asItem(), 0.01F));
		put(new Identifier("entities/chicken"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.CHICKEN).asItem(), 0.01F));
		put(new Identifier("entities/cow"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.COW).asItem(), 0.01F));
		put(new Identifier("entities/donkey"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.DONKEY).asItem(), 0.01F));
		put(new Identifier("entities/drowned"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.DROWNED).asItem(), 0.01F));
		put(new Identifier("entities/elder_guardian"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.ELDER_GUARDIAN).asItem(), 0.01F));
		put(new Identifier("entities/enderman"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.ENDERMAN).asItem(), 0.01F));
		put(new Identifier("entities/endermite"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.ENDERMITE).asItem(), 0.01F));
		put(new Identifier("entities/evoker"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.EVOKER).asItem(), 0.01F));
		put(new Identifier("entities/ghast"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.GHAST).asItem(), 0.01F));
		put(new Identifier("entities/guardian"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.GUARDIAN).asItem(), 0.01F));
		put(new Identifier("entities/hoglin"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.HOGLIN).asItem(), 0.01F));
		put(new Identifier("entities/horse"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.HORSE).asItem(), 0.01F));
		put(new Identifier("entities/husk"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.HUSK).asItem(), 0.01F));
		put(new Identifier("entities/illusioner"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.ILLUSIONER).asItem(), 0.01F));
		put(new Identifier("entities/iron_golem"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.IRON_GOLEM).asItem(), 0.01F));
		put(new Identifier("entities/llama"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.LLAMA).asItem(), 0.01F));
		put(new Identifier("entities/magma_cube"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.MAGMA_CUBE).asItem(), 0.01F));
		put(new Identifier("entities/mooshroom"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.MOOSHROOM).asItem(), 0.01F));
		put(new Identifier("entities/mule"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.MULE).asItem(), 0.01F));
		put(new Identifier("entities/ocelot"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.OCELOT).asItem(), 0.01F));
		put(new Identifier("entities/panda"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PANDA).asItem(), 0.01F));
		put(new Identifier("entities/phantom"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PHANTOM).asItem(), 0.01F));
		put(new Identifier("entities/pig"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PIG).asItem(), 0.01F));
		put(new Identifier("entities/piglin"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PIGLIN).asItem(), 0.01F));
		put(new Identifier("entities/polar_bear"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.POLAR_BEAR).asItem(), 0.01F));
		put(new Identifier("entities/pufferfish"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PUFFERFISH).asItem(), 0.01F));
		put(new Identifier("entities/rabbit"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.RABBIT).asItem(), 0.01F));
		put(new Identifier("entities/ravager"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.RAVAGER).asItem(), 0.01F));
		put(new Identifier("entities/salmon"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SALMON).asItem(), 0.01F));
		put(new Identifier("entities/silverfish"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SILVERFISH).asItem(), 0.01F));
		put(new Identifier("entities/slime"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SLIME).asItem(), 0.01F));
		put(new Identifier("entities/snow_golem"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SNOW_GOLEM).asItem(), 0.01F));
		put(new Identifier("entities/spider"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SPIDER).asItem(), 0.01F));
		put(new Identifier("entities/squid"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SQUID).asItem(), 0.01F));
		put(new Identifier("entities/stray"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.STRAY).asItem(), 0.01F));
		put(new Identifier("entities/strider"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.STRIDER).asItem(), 0.01F));
		put(new Identifier("entities/trader_llama"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.TRADER_LLAMA).asItem(), 0.01F));
		put(new Identifier("entities/turtle"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.TURTLE).asItem(), 0.01F));
		put(new Identifier("entities/vex"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.VEX).asItem(), 0.01F));
		put(new Identifier("entities/villager"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.VILLAGER).asItem(), 0.01F));
		put(new Identifier("entities/vindicator"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.VINDICATOR).asItem(), 0.01F));
		put(new Identifier("entities/wandering_trader"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.WANDERING_TRADER).asItem(), 0.01F));
		put(new Identifier("entities/wither"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.WITHER).asItem(), 0.15F)); // he has 3 heads, after all!
		put(new Identifier("entities/wolf"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.WOLF).asItem(), 0.01F));
		put(new Identifier("entities/zoglin"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.ZOGLIN).asItem(), 0.01F));
		put(new Identifier("entities/zombie_villager"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.ZOMBIE_VILLAGER).asItem(), 0.01F));
		put(new Identifier("entities/zombified_piglin"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.ZOMBIFIED_PIGLIN).asItem(), 0.01F));
		put(new Identifier("entities/bee"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.BEE).asItem(), 0.01F));
		put(new Identifier("entities/tropical_fish"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.CLOWNFISH).asItem(), 0.01F));
		put(new Identifier("entities/goat"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.GOAT).asItem(), 0.01F));
		put(new Identifier("entities/glow_squid"), new TreasureHunterDropDefinition(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.GLOW_SQUID).asItem(), 0.01F));
	}};

	private static final Map<Identifier, Item> resonanceBreakableLootPools = new HashMap<>() {{
		put(new Identifier("blocks/budding_amethyst"), Blocks.BUDDING_AMETHYST.asItem());
		put(new Identifier("blocks/infested_chiseled_stone_bricks"), Blocks.INFESTED_CHISELED_STONE_BRICKS.asItem());
		put(new Identifier("blocks/infested_cobblestone"), Blocks.INFESTED_COBBLESTONE.asItem());
		put(new Identifier("blocks/infested_cracked_stone_bricks"), Blocks.INFESTED_CRACKED_STONE_BRICKS.asItem());
		put(new Identifier("blocks/infested_mossy_stone_bricks"), Blocks.INFESTED_MOSSY_STONE_BRICKS.asItem());
		put(new Identifier("blocks/infested_stone"), Blocks.INFESTED_STONE.asItem());
		put(new Identifier("blocks/infested_stone_bricks"), Blocks.INFESTED_STONE_BRICKS.asItem());
		put(new Identifier("blocks/infested_deepslate"), Blocks.INFESTED_DEEPSLATE.asItem());
	}};

	public static void setup() {
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {

			if (resonanceBreakableLootPools.containsKey(id)) {
				Item resonanceDropItem = resonanceBreakableLootPools.get(id);

				// The item must have the enchantment
				EnchantmentPredicate resonanceEnchantmentPredicate = new EnchantmentPredicate(SpectrumEnchantments.RESONANCE, NumberRange.IntRange.atLeast(1));
				ItemPredicate.Builder itemPredicateBuilder = ItemPredicate.Builder.create().enchantment(resonanceEnchantmentPredicate);
				
				// the player must have completed the matching advancement
				PlayerPredicate hasAdvancementPredicateBuilder = PlayerPredicate.Builder.create().advancement(SpectrumEnchantments.RESONANCE.getUnlockAdvancementIdentifier(), true).build();
				EntityPredicate.Builder entityPredicateBuilder = EntityPredicate.Builder.create().player(hasAdvancementPredicateBuilder);
				LootCondition entityPropertiesLootCondition = EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, entityPredicateBuilder).build();
				
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.withCondition(MatchToolLootCondition.builder(itemPredicateBuilder).build())
						.withCondition(entityPropertiesLootCondition)
						.withEntry(ItemEntry.builder(resonanceDropItem).build());
				supplier.withPool(poolBuilder.build());
			}

			// TREASURE HUNTER POOLS
			// GENERIC
			if (trophyHunterLootPools.containsKey(id)) {
				TreasureHunterDropDefinition treasureHunterDropDefinition = trophyHunterLootPools.get(id);
				supplier.withPool(getLootPool(treasureHunterDropDefinition));
			}

			// Some entity types use custom loot conditions
			// because vanillas are too generic (fox/snow fox both use "fox" loot table)
			if(id.equals(new Identifier("entities/fox"))) {
				supplier.withPool(getFoxLootPool(FoxEntity.Type.RED, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.FOX).asItem(), 0.03F));
				supplier.withPool(getFoxLootPool(FoxEntity.Type.SNOW, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.FOX_ARCTIC).asItem(), 0.03F));
			}

			if(id.equals(new Identifier("entities/shulker"))) {
				supplier.withPool(getShulkerLootPool(DyeColor.BLACK, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_BLACK).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.BLUE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_BLUE).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.BROWN, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_BROWN).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.CYAN, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_CYAN).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.GRAY, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_GRAY).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.GREEN, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_GREEN).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.LIGHT_BLUE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_LIGHT_BLUE).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.LIGHT_GRAY, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_LIGHT_GRAY).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.LIME, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_LIME).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.MAGENTA, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_MAGENTA).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.ORANGE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_ORANGE).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.PINK, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_PINK).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.PURPLE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_PURPLE).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.RED, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_RED).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.WHITE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_WHITE).asItem(), 0.03F));
				supplier.withPool(getShulkerLootPool(DyeColor.YELLOW, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.SHULKER_YELLOW).asItem(), 0.03F));
			}

			if(id.equals(new Identifier("entities/axolotl"))) {
				supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.BLUE, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.AXOLOTL_BLUE).asItem(), 0.03F));
				supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.CYAN, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.AXOLOTL_CYAN).asItem(), 0.03F));
				supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.GOLD, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.AXOLOTL_GOLD).asItem(), 0.03F));
				supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.LUCY, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.AXOLOTL_LEUCISTIC).asItem(), 0.03F));
				supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.WILD, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.AXOLOTL_BROWN).asItem(), 0.03F));
			}

			if(id.equals(new Identifier("entities/parrot"))) {
				supplier.withPool(getParrotLootPool(0, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PARROT_RED).asItem(), 0.03F));
				supplier.withPool(getParrotLootPool(1, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PARROT_BLUE).asItem(), 0.03F));
				supplier.withPool(getParrotLootPool(2, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PARROT_GREEN).asItem(), 0.03F));
				supplier.withPool(getParrotLootPool(3, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PARROT_CYAN).asItem(), 0.03F));
				supplier.withPool(getParrotLootPool(4, SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PARROT_GRAY).asItem(), 0.03F));
			}

		});
	}

	private static LootPool getLootPool(TreasureHunterDropDefinition treasureHunterDropDefinition) {
		Item dropItem = treasureHunterDropDefinition.skullItem;
		float chance = treasureHunterDropDefinition.treasureHunterMultiplier;

		FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance, dropItem).build())
				.withEntry(ItemEntry.builder(dropItem).build());
		return poolBuilder.build();
	}

	private static LootPool getFoxLootPool(FoxEntity.Type foxType, Item item, float chance) {
		FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.withCondition(FoxTypeLootCondition.builder(foxType).build())
				.withEntry(ItemEntry.builder(item).build());
		return poolBuilder.build();
	}

	private static LootPool getShulkerLootPool(DyeColor dyeColor, Item item, float chance) {
		FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.withCondition(ShulkerColorLootCondition.builder(dyeColor).build())
				.withEntry(ItemEntry.builder(item).build());
		return poolBuilder.build();
	}

	private static LootPool getAxolotlLootPool(AxolotlEntity.Variant variant, Item item, float chance) {
		FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.withCondition(AxolotlVariantLootCondition.builder(variant).build())
				.withEntry(ItemEntry.builder(item).build());
		return poolBuilder.build();
	}


	private static LootPool getParrotLootPool(int variant, Item item, float chance) {
		FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance, item).build())
				.withCondition(ParrotVariantLootCondition.builder(variant).build())
				.withEntry(ItemEntry.builder(item).build());
		return poolBuilder.build();
	}


}
