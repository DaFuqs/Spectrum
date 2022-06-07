package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

public class ParrotVariantLootCondition implements LootCondition {
	
	int variant;
	
	private ParrotVariantLootCondition(int variant) {
		this.variant = variant;
	}
	
	public static Builder builder(int variant) {
		return () -> {
			return new ParrotVariantLootCondition(variant);
		};
	}
	
	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.PARROT_VARIANT_CONDITION;
	}
	
	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof ParrotEntity) {
			return ((ParrotEntity) entity).getVariant() == variant;
		} else {
			return false;
		}
	}
	
	public static class Serializer implements JsonSerializer<ParrotVariantLootCondition> {
		public Serializer() {
		}
		
		public void toJson(JsonObject jsonObject, ParrotVariantLootCondition axolotlVariantLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("parrot_variant", axolotlVariantLootCondition.variant);
		}
		
		public ParrotVariantLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			int parrotVariant = JsonHelper.getInt(jsonObject, "parrot_variant");
			return new ParrotVariantLootCondition(parrotVariant);
		}
	}
	
}
