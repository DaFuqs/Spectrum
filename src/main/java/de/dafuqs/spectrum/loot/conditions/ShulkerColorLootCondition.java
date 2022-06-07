package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.DyeColor;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import org.jetbrains.annotations.NotNull;

public class ShulkerColorLootCondition implements LootCondition {
	
	@NotNull
	DyeColor dyeColor;
	
	private ShulkerColorLootCondition(@NotNull DyeColor dyeColor) {
		this.dyeColor = dyeColor;
	}
	
	public static Builder builder(DyeColor dyeColor) {
		return () -> {
			return new ShulkerColorLootCondition(dyeColor);
		};
	}
	
	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.SHULKER_COLOR_CONDITION;
	}
	
	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof ShulkerEntity) {
			DyeColor dyeColor = ((ShulkerEntity) entity).getColor();
			if (dyeColor == null) {
				return this.dyeColor.equals(DyeColor.PURPLE);
			} else {
				return this.dyeColor.equals(dyeColor);
			}
		} else {
			return false;
		}
	}
	
	public static class Serializer implements JsonSerializer<ShulkerColorLootCondition> {
		public Serializer() {
		}
		
		public void toJson(JsonObject jsonObject, ShulkerColorLootCondition shulkerColorLootCondition, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("color", shulkerColorLootCondition.dyeColor.getName());
		}
		
		public ShulkerColorLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			String dyeColorString = JsonHelper.getString(jsonObject, "color");
			return new ShulkerColorLootCondition(DyeColor.byName(dyeColorString, DyeColor.PURPLE));
		}
	}
	
}
