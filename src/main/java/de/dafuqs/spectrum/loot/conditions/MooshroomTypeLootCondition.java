package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.*;

public class MooshroomTypeLootCondition implements LootCondition {
	
	final MooshroomEntity.Type mooshroomType;
	
	private MooshroomTypeLootCondition(MooshroomEntity.Type mooshroomType) {
		this.mooshroomType = mooshroomType;
	}
	
	public static Builder builder(MooshroomEntity.Type mooshroomType) {
		return () -> new MooshroomTypeLootCondition(mooshroomType);
	}
	
	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.MOOSHROOM_TYPE_CONDITION;
	}
	
	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof MooshroomEntity) {
			return ((MooshroomEntity) entity).getVariant().equals(mooshroomType);
		} else {
			return false;
		}
	}
	
	public static class Serializer implements JsonSerializer<MooshroomTypeLootCondition> {
		public Serializer() {
		}
		
		@Override
		public void toJson(JsonObject jsonObject, MooshroomTypeLootCondition mooshroomTypeLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("mooshroom_type", mooshroomTypeLootCondition.mooshroomType.toString());
		}
		
		@Override
		public MooshroomTypeLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			String mooshroomTypeString = JsonHelper.getString(jsonObject, "mooshroom_type");
			MooshroomEntity.Type mooshroomType = MooshroomEntity.Type.valueOf(mooshroomTypeString);
			return new MooshroomTypeLootCondition(mooshroomType);
		}
	}
	
}
