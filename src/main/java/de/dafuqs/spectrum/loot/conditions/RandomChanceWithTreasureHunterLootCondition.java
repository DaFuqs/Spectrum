package de.dafuqs.spectrum.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

import java.util.Set;

public class RandomChanceWithTreasureHunterLootCondition implements LootCondition {
	
	private final float chance;
	private final ItemStack advancementTriggerItemStack;
	
	private RandomChanceWithTreasureHunterLootCondition(float chance, Item item) {
		this.chance = chance;
		this.advancementTriggerItemStack = new ItemStack(item);
	}
	
	public static Builder builder(float chance, Item advancementTriggerItem) {
		return () -> new RandomChanceWithTreasureHunterLootCondition(chance, advancementTriggerItem);
	}
	
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.RANDOM_CHANCE_WITH_TREASURE_HUNTER;
	}
	
	public Set<LootContextParameter<?>> getRequiredParameters() {
		return ImmutableSet.of(LootContextParameters.KILLER_ENTITY);
	}
	
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.KILLER_ENTITY);
		int treasureHunterLevel = 0;
		if (entity instanceof PlayerEntity playerEntity) {
			if (!SpectrumEnchantments.TREASURE_HUNTER.canEntityUse(playerEntity)) {
				return false;
			}
			treasureHunterLevel = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.TREASURE_HUNTER, (LivingEntity) entity);
		}
		
		if (treasureHunterLevel == 0) {
			// No Treasure Hunter => no drop
			return false;
		} else {
			boolean success = lootContext.getRandom().nextFloat() < this.chance * treasureHunterLevel;
			if (success) {
				Entity killerEntity = lootContext.get(LootContextParameters.KILLER_ENTITY);
				if (killerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
					SpectrumAdvancementCriteria.TREASURE_HUNTER_DROP.trigger(serverPlayerEntity, advancementTriggerItemStack);
				}
			}
			return success;
		}
	}
	
	public static class Serializer implements JsonSerializer<RandomChanceWithTreasureHunterLootCondition> {
		
		public Serializer() {
		}
		
		public void toJson(JsonObject jsonObject, RandomChanceWithTreasureHunterLootCondition randomChanceWithLootingLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("chance", randomChanceWithLootingLootCondition.chance);
			jsonObject.addProperty("advancement_trigger_itemstack", Registry.ITEM.getId(randomChanceWithLootingLootCondition.advancementTriggerItemStack.getItem()).toString());
		}
		
		public RandomChanceWithTreasureHunterLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			return new RandomChanceWithTreasureHunterLootCondition(
					JsonHelper.getFloat(jsonObject, "chance"),
					Registry.ITEM.get(new Identifier(JsonHelper.getString(jsonObject, "advancement_trigger_itemstack")))
			);
		}
	}
}
