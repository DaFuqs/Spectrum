package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

public class FoxTypeLootCondition implements LootCondition {
	
	FoxEntity.Type foxType;
	
	private FoxTypeLootCondition(FoxEntity.Type foxType) {
		this.foxType = foxType;
	}
	
	public static Builder builder(FoxEntity.Type foxType) {
		return () -> {
			return new FoxTypeLootCondition(foxType);
		};
	}
	
	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.FOX_TYPE_CONDITION;
	}
	
	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof FoxEntity) {
			return ((FoxEntity) entity).getFoxType().equals(foxType);
		} else {
			return false;
		}
	}
	
	public static class Serializer implements JsonSerializer<FoxTypeLootCondition> {
		public Serializer() {
		}
		
		public void toJson(JsonObject jsonObject, FoxTypeLootCondition foxTypeLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("fox_type", foxTypeLootCondition.foxType.getKey());
		}
		
		public FoxTypeLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			String foxTypeString = JsonHelper.getString(jsonObject, "fox_type");
			FoxEntity.Type foxType = FoxEntity.Type.byName(foxTypeString);
			return new FoxTypeLootCondition(foxType);
		}
	}
	
}
