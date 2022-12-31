package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

public class FrogVariantLootCondition implements LootCondition {

	FrogVariant variant;

	private FrogVariantLootCondition(FrogVariant variant) {
		this.variant = variant;
	}

	public static Builder builder(FrogVariant variant) {
		return () -> new FrogVariantLootCondition(variant);
	}

	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.FROG_VARIANT_CONDITION;
	}

	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof FrogEntity frogEntity) {
			return frogEntity.getVariant().equals(variant);
		} else {
			return false;
		}
	}

	public static class Serializer implements JsonSerializer<FrogVariantLootCondition> {
		public Serializer() {
		}

		public void toJson(JsonObject jsonObject, FrogVariantLootCondition frogVariantLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("frog_variant", String.valueOf(Registry.FROG_VARIANT.getId(frogVariantLootCondition.variant)));
		}

		public FrogVariantLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			String variantString = JsonHelper.getString(jsonObject, "frog_variant");
			FrogVariant variant = Registry.FROG_VARIANT.get(Identifier.tryParse(variantString));
			return new FrogVariantLootCondition(variant);
		}
	}

}
