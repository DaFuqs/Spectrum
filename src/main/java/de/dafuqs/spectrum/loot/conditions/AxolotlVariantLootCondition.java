package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

public class AxolotlVariantLootCondition implements LootCondition {
	
	AxolotlEntity.Variant variant;
	
	private AxolotlVariantLootCondition(AxolotlEntity.Variant variant) {
		this.variant = variant;
	}
	
	public static Builder builder(AxolotlEntity.Variant variant) {
		return () -> {
			return new AxolotlVariantLootCondition(variant);
		};
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
		
		public void toJson(JsonObject jsonObject, AxolotlVariantLootCondition axolotlVariantLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("axolotl_variant", axolotlVariantLootCondition.variant.getName());
		}
		
		public AxolotlVariantLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			String axolotlVariant = JsonHelper.getString(jsonObject, "axolotl_variant");
			AxolotlEntity.Variant variant = AxolotlEntity.Variant.valueOf(axolotlVariant);
			return new AxolotlVariantLootCondition(variant);
		}
	}
	
}
