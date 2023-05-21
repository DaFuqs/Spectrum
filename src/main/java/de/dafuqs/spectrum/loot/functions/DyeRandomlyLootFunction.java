package de.dafuqs.spectrum.loot.functions;

import com.google.common.collect.*;
import com.google.gson.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.item.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.loot.function.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;

import java.util.*;

public class DyeRandomlyLootFunction extends ConditionalLootFunction {
	
	final List<Integer> colors;
	
	DyeRandomlyLootFunction(LootCondition[] conditions, Collection<Integer> colors) {
		super(conditions);
		this.colors = ImmutableList.copyOf(colors);
	}
	
	@Override
	public LootFunctionType getType() {
		return SpectrumLootFunctionTypes.DYE_RANDOMLY;
	}
	
	@Override
	public ItemStack process(ItemStack stack, LootContext context) {
		
		if (stack.getItem() instanceof DyeableItem dyeableItem) {
			Random random = context.getRandom();
			int color = this.colors.isEmpty() ? ColorHelper.getRandomColor(random.nextInt()) : this.colors.get(random.nextInt(this.colors.size()));
			dyeableItem.setColor(stack, color);
		}
		
		return stack;
	}
	
	public static Builder create() {
		return new Builder();
	}
	
	public static ConditionalLootFunction.Builder<?> builder() {
		return builder((conditions) -> new DyeRandomlyLootFunction(conditions, ImmutableList.of()));
	}
	
	public static class Builder extends ConditionalLootFunction.Builder<DyeRandomlyLootFunction.Builder> {
		private final Set<Integer> colors = Sets.newHashSet();
		
		@Override
		protected DyeRandomlyLootFunction.Builder getThisBuilder() {
			return this;
		}
		
		public DyeRandomlyLootFunction.Builder add(Integer color) {
			this.colors.add(color);
			return this;
		}
		
		@Override
		public LootFunction build() {
			return new DyeRandomlyLootFunction(this.getConditions(), this.colors);
		}
	}
	
	public static class Serializer extends ConditionalLootFunction.Serializer<DyeRandomlyLootFunction> {
		
		@Override
		public void toJson(JsonObject jsonObject, DyeRandomlyLootFunction lootFunction, JsonSerializationContext jsonSerializationContext) {
			super.toJson(jsonObject, lootFunction, jsonSerializationContext);
			if (!lootFunction.colors.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				for (Integer color : lootFunction.colors) {
					jsonArray.add(new JsonPrimitive(color));
				}
				jsonObject.add("colors", jsonArray);
			}
		}
		
		@Override
		public DyeRandomlyLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
			List<Integer> colors = Lists.newArrayList();
			if (jsonObject.has("colors")) {
				JsonArray jsonArray = JsonHelper.getArray(jsonObject, "colors");
				for (JsonElement jsonElement : jsonArray) {
					if (jsonElement instanceof JsonPrimitive jsonPrimitive) {
						if (jsonPrimitive.isNumber()) {
							colors.add(jsonElement.getAsInt());
						} else if (jsonPrimitive.isString()) {
							String hex = jsonPrimitive.getAsString();
							if (hex.startsWith("#")) {
								colors.add(Integer.parseInt(hex.substring(1), 16));
							}
						}
					}
				}
			}
			
			return new DyeRandomlyLootFunction(lootConditions, colors);
		}
	}
}
