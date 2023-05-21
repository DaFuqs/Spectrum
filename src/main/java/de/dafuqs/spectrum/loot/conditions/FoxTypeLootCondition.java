package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.*;

public class FoxTypeLootCondition implements LootCondition {
	
	final FoxEntity.Type foxType;
	
	private FoxTypeLootCondition(FoxEntity.Type foxType) {
		this.foxType = foxType;
	}
	
	public static Builder builder(FoxEntity.Type foxType) {
		return () -> new FoxTypeLootCondition(foxType);
	}
	
	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.FOX_TYPE_CONDITION;
	}
	
	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof FoxEntity) {
			return ((FoxEntity) entity).getVariant().equals(foxType);
		} else {
			return false;
		}
	}
	
	public static class Serializer implements JsonSerializer<FoxTypeLootCondition> {
		public Serializer() {
		}
		
		@Override
		public void toJson(JsonObject jsonObject, FoxTypeLootCondition foxTypeLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("fox_type", foxTypeLootCondition.foxType.asString());
		}
		
		@Override
		public FoxTypeLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			String foxTypeString = JsonHelper.getString(jsonObject, "fox_type");
			FoxEntity.Type foxType = FoxEntity.Type.byName(foxTypeString);
			return new FoxTypeLootCondition(foxType);
		}
	}
	
}
