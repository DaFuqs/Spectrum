package de.dafuqs.pigment.loot;

import de.dafuqs.pigment.blocks.head.PigmentSkullBlock;
import de.dafuqs.pigment.enchantments.PigmentEnchantments;
import de.dafuqs.pigment.registries.PigmentBlocks;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentDrops {


    private static class TreasureHunterDropDefinition {
        public PigmentSkullBlock.Type skullType;
        public float treasureHunterMultiplier;

        public TreasureHunterDropDefinition(PigmentSkullBlock.Type skullType, float trophyHunterChance) {
            this.skullType = skullType;
            this.treasureHunterMultiplier = trophyHunterChance;
        }
    }

    private static final Map<Identifier, TreasureHunterDropDefinition> trophyHunterLootPools = new HashMap<Identifier, TreasureHunterDropDefinition>() {{
        put(new Identifier("entities/bee"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.BEE, 0.01F));
        put(new Identifier("entities/blaze"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.BLAZE, 0.01F));
        put(new Identifier("entities/cave_spider"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.CAVE_SPIDER, 0.01F));
        put(new Identifier("entities/chicken"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.CHICKEN, 0.01F));
        put(new Identifier("entities/cow"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.COW, 0.2F));
        put(new Identifier("entities/donkey"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.DONKEY, 0.01F));
        put(new Identifier("entities/drowned"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.DROWNED, 0.01F));
        put(new Identifier("entities/elder_guardian"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.ELDER_GUARDIAN, 0.01F));
        put(new Identifier("entities/enderman"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.ENDERMAN, 0.01F));
        put(new Identifier("entities/endermite"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.ENDERMITE, 0.01F));
        put(new Identifier("entities/evoker"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.EVOKER, 0.01F));
        put(new Identifier("entities/fox"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.FOX, 0.01F));
        put(new Identifier("entities/ghast"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.GHAST, 0.01F));
        put(new Identifier("entities/guardian"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.GUARDIAN, 0.01F));
        put(new Identifier("entities/hoglin"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.HOGLIN, 0.01F));
        put(new Identifier("entities/horse"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.HORSE, 0.01F));
        put(new Identifier("entities/husk"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.HUSK, 0.01F));
        put(new Identifier("entities/iron_golem"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.IRON_GOLEM, 0.01F));
        put(new Identifier("entities/llama"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.LLAMA, 0.01F));
        put(new Identifier("entities/magma_cube"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.MAGMA_CUBE, 0.01F));
        put(new Identifier("entities/mooshroom"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.MOOSHROOM, 0.01F));
        put(new Identifier("entities/mule"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.MULE, 0.01F));
        put(new Identifier("entities/ocelot"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.OCELOT, 0.01F));
        put(new Identifier("entities/panda"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.PANDA, 0.01F));
        put(new Identifier("entities/parrot"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.PARROT, 0.01F));
        put(new Identifier("entities/phantom"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.PHANTOM, 0.01F));
        put(new Identifier("entities/pig"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.PIG, 0.01F));
        put(new Identifier("entities/piglin"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.PIGLIN, 0.01F));
        put(new Identifier("entities/polar_bear"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.POLAR_BEAR, 0.01F));
        put(new Identifier("entities/rabbit"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.RABBIT, 0.01F));
        put(new Identifier("entities/sheep"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.SHEEP, 0.01F));
        put(new Identifier("entities/shulker"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.SHULKER, 0.01F));
        put(new Identifier("entities/silverfish"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.SILVERFISH, 0.01F));
        put(new Identifier("entities/slime"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.SLIME, 0.01F));
        put(new Identifier("entities/spider"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.SPIDER, 0.01F));
        put(new Identifier("entities/squid"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.SQUID, 0.01F));
        put(new Identifier("entities/stray"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.STRAY, 0.01F));
        put(new Identifier("entities/strider"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.STRIDER, 0.01F));
        put(new Identifier("entities/turtle"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.TURTLE, 0.01F));
        put(new Identifier("entities/villager"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.VILLAGER, 0.01F));
        put(new Identifier("entities/wolf"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.WOLF, 0.01F));
        put(new Identifier("entities/zoglin"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.ZOGLIN, 0.01F));
        put(new Identifier("entities/zombified_piglin"), new TreasureHunterDropDefinition(PigmentSkullBlock.Type.ZOMBIFIED_PIGLIN, 0.01F));
    }};

    private static final Map<Identifier, Item> resonanceBreakableLootPools = new HashMap<Identifier, Item>() {{
        put(new Identifier("blocks/budding_amethyst"), Blocks.BUDDING_AMETHYST.asItem());
        put(new Identifier("blocks/infested_chiseled_stone_bricks"), Blocks.INFESTED_CHISELED_STONE_BRICKS.asItem());
        put(new Identifier("blocks/infested_cobblestone"), Blocks.INFESTED_COBBLESTONE.asItem());
        put(new Identifier("blocks/infested_cracked_stone_bricks"), Blocks.INFESTED_CRACKED_STONE_BRICKS.asItem());
        put(new Identifier("blocks/infested_mossy_stone_bricks"), Blocks.INFESTED_MOSSY_STONE_BRICKS.asItem());
        put(new Identifier("blocks/infested_stone"), Blocks.INFESTED_STONE.asItem());
        put(new Identifier("blocks/infested_stone_bricks"), Blocks.INFESTED_STONE_BRICKS.asItem());
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

            if (trophyHunterLootPools.containsKey(id)) {
                TreasureHunterDropDefinition treasureHunterDropDefinition = trophyHunterLootPools.get(id);
                Item dropItem = PigmentBlocks.getMobHead(treasureHunterDropDefinition.skullType).asItem();
                float chance = treasureHunterDropDefinition.treasureHunterMultiplier;

                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .withCondition(RandomChanceWithTreasureHunterLootCondition.builder(chance).build())
                        .withEntry(ItemEntry.builder(dropItem).build());
                supplier.withPool(poolBuilder.build());
            }


        });
    }

}
