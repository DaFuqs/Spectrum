package de.dafuqs.spectrum.recipe.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class RecipeUtils {

	public static DefaultedList<ItemStack> deserializeItems(JsonElement jsonObject) {
		if (jsonObject.isJsonArray()) {
			return SerializationUtil.stream(jsonObject.getAsJsonArray()).map(entry -> deserializeItem(entry.getAsJsonObject())).collect(DefaultedListCollector.toList());
		} else {
			return DefaultedList.copyOf(deserializeItem(jsonObject.getAsJsonObject()));
		}
	}

	/**
	 * Deserializes an item from a recipe to an itemstack
	 * @param jsonObject The json object to serialize
	 * @return The serializes itemstack
	 */
	private static ItemStack deserializeItem(JsonObject jsonObject) {
		Identifier resourceLocation = new Identifier(JsonHelper.getString(jsonObject, "item"));
		Item item = Registry.ITEM.get(resourceLocation);
		if (item == Items.AIR) {
			throw new IllegalStateException(resourceLocation + " did not exist");
		}
		int count = 1;
		if (jsonObject.has("count")) {
			count = JsonHelper.getInt(jsonObject, "count");
		}
		ItemStack stack = new ItemStack(item, count);
		if (jsonObject.has("nbt")) {
			CompoundTag tag = (CompoundTag) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, jsonObject.get("nbt"));
			stack.setTag(tag);
		}
		return stack;
	}

}