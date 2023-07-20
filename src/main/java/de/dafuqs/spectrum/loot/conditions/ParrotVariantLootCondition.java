package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.*;

public class ParrotVariantLootCondition implements LootCondition {
	
	final int variant;
	
	private ParrotVariantLootCondition(int variant) {
		this.variant = variant;
	}
	
	public static Builder builder(int variant) {
		return () -> new ParrotVariantLootCondition(variant);
	}
	
	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.PARROT_VARIANT_CONDITION;
	}
	
	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof ParrotEntity) {
			return ((ParrotEntity) entity).getVariant().getId() == variant;
		} else {
			return false;
		}
	}
	
	public static class Serializer implements JsonSerializer<ParrotVariantLootCondition> {
		public Serializer() {
		}
		
		@Override
		public void toJson(JsonObject jsonObject, ParrotVariantLootCondition axolotlVariantLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("parrot_variant", axolotlVariantLootCondition.variant);
		}
		
		@Override
		public ParrotVariantLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			int parrotVariant = JsonHelper.getInt(jsonObject, "parrot_variant");
			return new ParrotVariantLootCondition(parrotVariant);
		}
	}
	
}
