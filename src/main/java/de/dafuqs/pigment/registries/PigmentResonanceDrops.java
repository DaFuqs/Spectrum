package de.dafuqs.pigment.registries;

import de.dafuqs.pigment.enchantments.PigmentEnchantments;
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

public class PigmentResonanceDrops {

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
        });
    }

}
