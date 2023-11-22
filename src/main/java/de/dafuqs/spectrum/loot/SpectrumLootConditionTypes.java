package de.dafuqs.spectrum.loot;

import de.dafuqs.spectrum.loot.conditions.*;
import net.minecraft.loot.condition.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

public class SpectrumLootConditionTypes {

    public static LootConditionType RANDOM_CHANCE_WITH_TREASURE_HUNTER;

    private static LootConditionType register(String id, JsonSerializer<? extends LootCondition> serializer) {
		return Registry.register(Registries.LOOT_CONDITION_TYPE, new Identifier(id), new LootConditionType(serializer));
    }

    public static void register() {
        RANDOM_CHANCE_WITH_TREASURE_HUNTER = register("random_chance_with_treasure_hunter", new RandomChanceWithTreasureHunterLootCondition.Serializer());
	}
	
}
