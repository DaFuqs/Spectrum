package de.dafuqs.spectrum.loot;

import de.dafuqs.spectrum.loot.conditions.*;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

public class SpectrumLootConditionTypes {
	
	public static LootConditionType RANDOM_CHANCE_WITH_TREASURE_HUNTER;
	
	public static LootConditionType AXOLOTL_VARIANT_CONDITION;
	public static LootConditionType SHULKER_COLOR_CONDITION;
	public static LootConditionType FOX_TYPE_CONDITION;
	public static LootConditionType PARROT_VARIANT_CONDITION;
	public static LootConditionType MOOSHROOM_TYPE_CONDITION;
	
	private static LootConditionType register(String id, JsonSerializer<? extends LootCondition> serializer) {
		return Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(id), new LootConditionType(serializer));
	}
	
	public static void register() {
		RANDOM_CHANCE_WITH_TREASURE_HUNTER = register("random_chance_with_treasure_hunter", new RandomChanceWithTreasureHunterLootCondition.Serializer());
		
		FOX_TYPE_CONDITION = register("fox_type", new FoxTypeLootCondition.Serializer());
		AXOLOTL_VARIANT_CONDITION = register("axolotl_variant", new AxolotlVariantLootCondition.Serializer());
		SHULKER_COLOR_CONDITION = register("shulker_color", new ShulkerColorLootCondition.Serializer());
		PARROT_VARIANT_CONDITION = register("parrot_variant", new ParrotVariantLootCondition.Serializer());
		MOOSHROOM_TYPE_CONDITION = register("mooshroom_type", new MooshroomTypeLootCondition.Serializer());
	}
	
}
