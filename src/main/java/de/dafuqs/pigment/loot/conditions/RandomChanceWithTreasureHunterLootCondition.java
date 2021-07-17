package de.dafuqs.pigment.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.dafuqs.pigment.enchantments.PigmentEnchantments;
import de.dafuqs.pigment.loot.PigmentLootConditionTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

import java.util.Set;

public class RandomChanceWithTreasureHunterLootCondition implements LootCondition {

    private final float chance;

    private RandomChanceWithTreasureHunterLootCondition(float chance) {
        this.chance = chance;
    }

    public LootConditionType getType() {
        return PigmentLootConditionTypes.RANDOM_CHANCE_WITH_TREASURE_HUNTER;
    }

    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.KILLER_ENTITY);
    }

    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.get(LootContextParameters.KILLER_ENTITY);
        int treasureHunterLevel = 0;
        if (entity instanceof LivingEntity) {
            treasureHunterLevel = EnchantmentHelper.getEquipmentLevel(PigmentEnchantments.TREASURE_HUNTER, (LivingEntity)entity);
        }

        if(treasureHunterLevel == 0) {
            // No Treasure Hunter => no drop
            return false;
        } else {
            return lootContext.getRandom().nextFloat() < this.chance * treasureHunterLevel;
        }
    }

    public static Builder builder(float chance) {
        return () -> {
            return new RandomChanceWithTreasureHunterLootCondition(chance);
        };
    }

    public static class Serializer implements JsonSerializer<RandomChanceWithTreasureHunterLootCondition> {
        public Serializer() {
        }

        public void toJson(JsonObject jsonObject, RandomChanceWithTreasureHunterLootCondition randomChanceWithLootingLootCondition, JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("chance", randomChanceWithLootingLootCondition.chance);
        }

        public RandomChanceWithTreasureHunterLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return new RandomChanceWithTreasureHunterLootCondition(JsonHelper.getFloat(jsonObject, "chance"));
        }
    }
}
