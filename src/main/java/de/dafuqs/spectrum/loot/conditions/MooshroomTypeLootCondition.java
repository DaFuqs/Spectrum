package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

public class MooshroomTypeLootCondition implements LootCondition {
	
	MooshroomEntity.Type mooshroomType;
	
	private MooshroomTypeLootCondition(MooshroomEntity.Type mooshroomType) {
		this.mooshroomType = mooshroomType;
	}
	
	public static Builder builder(MooshroomEntity.Type mooshroomType) {
		return () -> {
			return new MooshroomTypeLootCondition(mooshroomType);
		};
	}
	
	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.MOOSHROOM_TYPE_CONDITION;
	}
	
	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof MooshroomEntity) {
			return ((MooshroomEntity) entity).getMooshroomType().equals(mooshroomType);
		} else {
			return false;
		}
	}
	
	public static class Serializer implements JsonSerializer<MooshroomTypeLootCondition> {
		public Serializer() {
		}
		
		public void toJson(JsonObject jsonObject, MooshroomTypeLootCondition mooshroomTypeLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("mooshroom_type", mooshroomTypeLootCondition.mooshroomType.toString());
		}
		
		public MooshroomTypeLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			String mooshroomTypeString = JsonHelper.getString(jsonObject, "mooshroom_type");
			MooshroomEntity.Type mooshroomType = MooshroomEntity.Type.valueOf(mooshroomTypeString);
			return new MooshroomTypeLootCondition(mooshroomType);
		}
	}
	
}
