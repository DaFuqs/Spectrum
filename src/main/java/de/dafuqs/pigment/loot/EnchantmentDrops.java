package de.dafuqs.pigment.loot;

import de.dafuqs.pigment.blocks.mob_head.PigmentSkullBlock;
import de.dafuqs.pigment.enchantments.PigmentEnchantments;
import de.dafuqs.pigment.loot.conditions.*;
import de.dafuqs.pigment.registries.PigmentBlocks;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.NumberRange;
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

    private static final Map<Identifier, TreasureHunterDropDefinition> trophyHunterLootPools = new HashMap<Identifier, TreasureHunterDropDefinition>() {{
        // Additional vanilla head drops
        put(new Identifier("entities/creeper"), new TreasureHunterDropDefinition(Items.CREEPER_HEAD, 0.02F));
        put(new Identifier("entities/skeleton"), new TreasureHunterDropDefinition(Items.SKELETON_SKULL, 0.02F));
        put(new Identifier("entities/wither_skeleton"), new TreasureHunterDropDefinition(Items.WITHER_SKELETON_SKULL, 0.1F));
        put(new Identifier("entities/zombie"), new TreasureHunterDropDefinition(Items.ZOMBIE_HEAD, 0.02F));
        put(new Identifier("entities/ender_dragon"), new TreasureHunterDropDefinition(Items.DRAGON_HEAD, 0.1F)); // why not!

        // Pigment head drops
        // ATTENTION: No specific enough loot tables exist for fox, axolotl, parrot and shulker variants.
        // Those are handled separately in setup()
        put(new Identifier("entities/sheep/black"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_BLACK).asItem(), 0.01F));
        put(new Identifier("entities/sheep/blue"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_BLUE).asItem(), 0.01F));
        put(new Identifier("entities/sheep/brown"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_BROWN).asItem(), 0.01F));
        put(new Identifier("entities/sheep/cyan"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_CYAN).asItem(), 0.01F));
        put(new Identifier("entities/sheep/gray"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_GRAY).asItem(), 0.01F));
        put(new Identifier("entities/sheep/green"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_GREEN).asItem(), 0.01F));
        put(new Identifier("entities/sheep/light_blue"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_LIGHT_BLUE).asItem(), 0.01F));
        put(new Identifier("entities/sheep/light_gray"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_LIGHT_GRAY).asItem(), 0.01F));
        put(new Identifier("entities/sheep/lime"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_LIME).asItem(), 0.01F));
        put(new Identifier("entities/sheep/magenta"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_MAGENTA).asItem(), 0.01F));
        put(new Identifier("entities/sheep/orange"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_ORANGE).asItem(), 0.01F));
        put(new Identifier("entities/sheep/pink"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_PINK).asItem(), 0.01F));
        put(new Identifier("entities/sheep/purple"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_PURPLE).asItem(), 0.01F));
        put(new Identifier("entities/sheep/red"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_RED).asItem(), 0.01F));
        put(new Identifier("entities/sheep/white"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_WHITE).asItem(), 0.01F));
        put(new Identifier("entities/sheep/yellow"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHEEP_YELLOW).asItem(), 0.01F));
        put(new Identifier("entities/bat"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.BAT).asItem(), 0.01F));
        put(new Identifier("entities/blaze"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.BLAZE).asItem(), 0.01F));
        put(new Identifier("entities/cat"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.CAT).asItem(), 0.01F));
        put(new Identifier("entities/cave_spider"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.CAVE_SPIDER).asItem(), 0.01F));
        put(new Identifier("entities/chicken"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.CHICKEN).asItem(), 0.01F));
        put(new Identifier("entities/cow"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.COW).asItem(), 0.01F));
        put(new Identifier("entities/donkey"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.DONKEY).asItem(), 0.01F));
        put(new Identifier("entities/drowned"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.DROWNED).asItem(), 0.01F));
        put(new Identifier("entities/elder_guardian"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.ELDER_GUARDIAN).asItem(), 0.01F));
        put(new Identifier("entities/enderman"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.ENDERMAN).asItem(), 0.01F));
        put(new Identifier("entities/endermite"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.ENDERMITE).asItem(), 0.01F));
        put(new Identifier("entities/evoker"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.EVOKER).asItem(), 0.01F));
        put(new Identifier("entities/ghast"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.GHAST).asItem(), 0.01F));
        put(new Identifier("entities/guardian"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.GUARDIAN).asItem(), 0.01F));
        put(new Identifier("entities/hoglin"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.HOGLIN).asItem(), 0.01F));
        put(new Identifier("entities/horse"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.HORSE).asItem(), 0.01F));
        put(new Identifier("entities/husk"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.HUSK).asItem(), 0.01F));
        put(new Identifier("entities/illusioner"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.ILLUSIONER).asItem(), 0.01F));
        put(new Identifier("entities/iron_golem"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.IRON_GOLEM).asItem(), 0.01F));
        put(new Identifier("entities/llama"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.LLAMA).asItem(), 0.01F));
        put(new Identifier("entities/magma_cube"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.MAGMA_CUBE).asItem(), 0.01F));
        put(new Identifier("entities/mooshroom"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.MOOSHROOM).asItem(), 0.01F));
        put(new Identifier("entities/mule"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.MULE).asItem(), 0.01F));
        put(new Identifier("entities/ocelot"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.OCELOT).asItem(), 0.01F));
        put(new Identifier("entities/panda"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PANDA).asItem(), 0.01F));
        put(new Identifier("entities/phantom"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PHANTOM).asItem(), 0.01F));
        put(new Identifier("entities/pig"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PIG).asItem(), 0.01F));
        put(new Identifier("entities/piglin"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PIGLIN).asItem(), 0.01F));
        put(new Identifier("entities/polar_bear"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.POLAR_BEAR).asItem(), 0.01F));
        put(new Identifier("entities/pufferfish"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PUFFERFISH).asItem(), 0.01F));
        put(new Identifier("entities/rabbit"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.RABBIT).asItem(), 0.01F));
        put(new Identifier("entities/ravager"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.RAVAGER).asItem(), 0.01F));
        put(new Identifier("entities/salmon"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SALMON).asItem(), 0.01F));
        put(new Identifier("entities/silverfish"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SILVERFISH).asItem(), 0.01F));
        put(new Identifier("entities/slime"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SLIME).asItem(), 0.01F));
        put(new Identifier("entities/snow_golem"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SNOW_GOLEM).asItem(), 0.01F));
        put(new Identifier("entities/spider"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SPIDER).asItem(), 0.01F));
        put(new Identifier("entities/squid"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SQUID).asItem(), 0.01F));
        put(new Identifier("entities/stray"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.STRAY).asItem(), 0.01F));
        put(new Identifier("entities/strider"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.STRIDER).asItem(), 0.01F));
        put(new Identifier("entities/trader_llama"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.TRADER_LLAMA).asItem(), 0.01F));
        put(new Identifier("entities/turtle"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.TURTLE).asItem(), 0.01F));
        put(new Identifier("entities/vex"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.VEX).asItem(), 0.01F));
        put(new Identifier("entities/villager"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.VILLAGER).asItem(), 0.01F));
        put(new Identifier("entities/vindicator"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.VINDICATOR).asItem(), 0.01F));
        put(new Identifier("entities/wandering_trader"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.WANDERING_TRADER).asItem(), 0.01F));
        put(new Identifier("entities/wither"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.WITHER).asItem(), 0.01F));
        put(new Identifier("entities/wolf"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.WOLF).asItem(), 0.01F));
        put(new Identifier("entities/zoglin"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.ZOGLIN).asItem(), 0.01F));
        put(new Identifier("entities/zombie_villager"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.ZOMBIE_VILLAGER).asItem(), 0.01F));
        put(new Identifier("entities/zombified_piglin"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.ZOMBIFIED_PIGLIN).asItem(), 0.01F));
        put(new Identifier("entities/bee"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.BEE).asItem(), 0.01F));
        put(new Identifier("entities/tropical_fish"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.CLOWNFISH).asItem(), 0.01F));
        put(new Identifier("entities/goat"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.GOAT).asItem(), 0.01F));
        put(new Identifier("entities/glow_squid"), new TreasureHunterDropDefinition(PigmentBlocks.getMobHead(PigmentSkullBlock.Type.GLOW_SQUID).asItem(), 0.01F));
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

                EnchantmentPredicate resonanceEnchantmentPredicate = new EnchantmentPredicate(PigmentEnchantments.RESONANCE, NumberRange.IntRange.atLeast(1));
                ItemPredicate.Builder itemPredicateBuilder = ItemPredicate.Builder.create().enchantment(resonanceEnchantmentPredicate);

                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .withCondition(MatchToolLootCondition.builder(itemPredicateBuilder).build())
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
                supplier.withPool(getFoxLootPool(FoxEntity.Type.RED, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.FOX).asItem(), 0.03F));
                supplier.withPool(getFoxLootPool(FoxEntity.Type.SNOW, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.FOX_ARCTIC).asItem(), 0.03F));
            }

            if(id.equals(new Identifier("entities/shulker"))) {
                supplier.withPool(getShulkerLootPool(DyeColor.BLACK, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_BLACK).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.BLUE, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_BLUE).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.BROWN, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_BROWN).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.CYAN, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_CYAN).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.GRAY, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_GRAY).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.GREEN, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_GREEN).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.LIGHT_BLUE, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_LIGHT_BLUE).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.LIGHT_GRAY, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_LIGHT_GRAY).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.LIME, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_LIME).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.MAGENTA, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_MAGENTA).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.ORANGE, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_ORANGE).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.PINK, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_PINK).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.PURPLE, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_PURPLE).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.RED, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_RED).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.WHITE, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_WHITE).asItem(), 0.03F));
                supplier.withPool(getShulkerLootPool(DyeColor.YELLOW, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.SHULKER_YELLOW).asItem(), 0.03F));
            }

            if(id.equals(new Identifier("entities/axolotl"))) {
                supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.BLUE, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.AXOLOTL_BLUE).asItem(), 0.03F));
                supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.CYAN, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.AXOLOTL_CYAN).asItem(), 0.03F));
                supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.GOLD, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.AXOLOTL_GOLD).asItem(), 0.03F));
                supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.LUCY, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.AXOLOTL_LEUCISTIC).asItem(), 0.03F));
                supplier.withPool(getAxolotlLootPool(AxolotlEntity.Variant.WILD, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.AXOLOTL_BROWN).asItem(), 0.03F));
            }

            if(id.equals(new Identifier("entities/parrot"))) {
                supplier.withPool(getParrotLootPool(0, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PARROT_RED).asItem(), 0.03F));
                supplier.withPool(getParrotLootPool(1, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PARROT_BLUE).asItem(), 0.03F));
                supplier.withPool(getParrotLootPool(2, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PARROT_GREEN).asItem(), 0.03F));
                supplier.withPool(getParrotLootPool(3, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PARROT_CYAN).asItem(), 0.03F));
                supplier.withPool(getParrotLootPool(4, PigmentBlocks.getMobHead(PigmentSkullBlock.Type.PARROT_GRAY).asItem(), 0.03F));
            }

        });
    }

    private static LootPool getLootPool(TreasureHunterDropDefinition treasureHunterDropDefinition) {
        Item dropItem = treasureHunterDropDefinition.skullItem;
        float chance = treasureHunterDropDefinition.treasureHunterMultiplier;

        FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance).build())
                .withEntry(ItemEntry.builder(dropItem).build());
        return poolBuilder.build();
    }

    private static LootPool getFoxLootPool(FoxEntity.Type foxType, Item item, float chance) {
        FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance).build())
                .withCondition(FoxTypeLootCondition.builder(foxType).build())
                .withEntry(ItemEntry.builder(item).build());
        return poolBuilder.build();
    }

    private static LootPool getShulkerLootPool(DyeColor dyeColor, Item item, float chance) {
        FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance).build())
                .withCondition(ShulkerColorLootCondition.builder(dyeColor).build())
                .withEntry(ItemEntry.builder(item).build());
        return poolBuilder.build();
    }

    private static LootPool getAxolotlLootPool(AxolotlEntity.Variant variant, Item item, float chance) {
        FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance).build())
                .withCondition(AxolotlVariantLootCondition.builder(variant).build())
                .withEntry(ItemEntry.builder(item).build());
        return poolBuilder.build();
    }


    private static LootPool getParrotLootPool(int variant, Item item, float chance) {
        FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance).build())
                .withCondition(ParrotVariantLootCondition.builder(variant).build())
                .withEntry(ItemEntry.builder(item).build());
        return poolBuilder.build();
    }


}
