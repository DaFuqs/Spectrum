package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.*;

public class AxolotlVariantLootCondition implements LootCondition {
	
	final AxolotlEntity.Variant variant;
	
	private AxolotlVariantLootCondition(AxolotlEntity.Variant variant) {
		this.variant = variant;
	}
	
	public static Builder builder(AxolotlEntity.Variant variant) {
		return () -> new AxolotlVariantLootCondition(variant);
	}
	
	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.AXOLOTL_VARIANT_CONDITION;
	}
	
	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof AxolotlEntity) {
			return ((AxolotlEntity) entity).getVariant().equals(variant);
		} else {
			return false;
		}
	}
	
	public static class Serializer implements JsonSerializer<AxolotlVariantLootCondition> {
		public Serializer() {
		}
		
		@Override
		public void toJson(JsonObject jsonObject, AxolotlVariantLootCondition axolotlVariantLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("axolotl_variant", axolotlVariantLootCondition.variant.getName());
		}
		
		@Override
		public AxolotlVariantLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			String axolotlVariant = JsonHelper.getString(jsonObject, "axolotl_variant");
			AxolotlEntity.Variant variant = AxolotlEntity.Variant.valueOf(axolotlVariant);
			return new AxolotlVariantLootCondition(variant);
		}
	}
	
}
