package de.dafuqs.pigment.loot;

import net.minecraft.loot.condition.InvertedLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

public class PigmentLootConditionTypes {

    public static LootConditionType RANDOM_CHANCE_WITH_TREASURE_HUNTER;

    private static LootConditionType register(String id, JsonSerializer<? extends LootCondition> serializer) {
        return  Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(id), new LootConditionType(serializer));
    }

    public static void register() {
        RANDOM_CHANCE_WITH_TREASURE_HUNTER = register("random_chance_with_treasure_hunter", new InvertedLootCondition.Serializer());
    }

}
