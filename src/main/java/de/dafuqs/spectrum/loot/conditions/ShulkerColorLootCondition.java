package de.dafuqs.spectrum.loot.conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public class ShulkerColorLootCondition implements LootCondition {

	private final @Nullable DyeColor dyeColor;

	private ShulkerColorLootCondition(@Nullable DyeColor dyeColor) {
		this.dyeColor = dyeColor;
	}

	public static Builder builder(DyeColor dyeColor) {
		return () -> new ShulkerColorLootCondition(dyeColor);
	}

	@Override
	public LootConditionType getType() {
		return SpectrumLootConditionTypes.SHULKER_COLOR_CONDITION;
	}
	
	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity instanceof ShulkerEntity) {
			@Nullable DyeColor shulkerColor = ((ShulkerEntity) entity).getColor();
			if (this.dyeColor == null) {
				return shulkerColor == null;
			} else {
				return this.dyeColor.equals(shulkerColor);
			}
		} else {
			return false;
		}
	}
	
	public static class Serializer implements JsonSerializer<ShulkerColorLootCondition> {
		public Serializer() {
		}
		
		@Override
		public void toJson(JsonObject jsonObject, ShulkerColorLootCondition shulkerColorLootCondition, JsonSerializationContext jsonSerializationContext) {
			if (shulkerColorLootCondition.dyeColor != null) {
				jsonObject.addProperty("color", shulkerColorLootCondition.dyeColor.getName());
			}
		}
		
		@Override
		public ShulkerColorLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			String dyeColorString = JsonHelper.getString(jsonObject, "color", "");
			if (dyeColorString.isEmpty()) {
				new ShulkerColorLootCondition(null);
			}
			return new ShulkerColorLootCondition(DyeColor.byName(dyeColorString, DyeColor.PURPLE));
		}
	}
	
}
