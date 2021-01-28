/*package de.dafuqs.spectrum.recipe.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

	private static final String NAME = "name";
	private static final String STACK_SIZE = "stackSize";
	private static final String TAG_COMPOUND = "tagCompound";

	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (json.isJsonObject()) {

			JsonObject jsonObject = json.getAsJsonObject();

			String name = null;
			int stackSize = 1;
			CompoundTag tagCompound = null;

			if (jsonObject.has(NAME) && jsonObject.get(NAME).isJsonPrimitive()) {
				name = jsonObject.getAsJsonPrimitive(NAME).getAsString();
			}

			if (jsonObject.has(STACK_SIZE) && jsonObject.get(STACK_SIZE).isJsonPrimitive()) {
				stackSize = jsonObject.getAsJsonPrimitive(STACK_SIZE).getAsInt();
			}

			if (jsonObject.has(TAG_COMPOUND) && jsonObject.get(TAG_COMPOUND).isJsonPrimitive()) {
				try {
					tagCompound = StringNbtReader.parse(jsonObject.getAsJsonPrimitive(TAG_COMPOUND).getAsString());
				} catch (CommandSyntaxException ignored) {

				}
			}

			if (name != null) {
				Registry.ITEM.get(new Identifier(name));
				ItemStack itemStack = new ItemStack(Registry.ITEM.get(new Identifier(name)), stackSize);
				itemStack.setTag(tagCompound);
				return itemStack;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
		if (src != null && src.getItem() != null) {
			JsonObject jsonObject = new JsonObject();

			Registry.ITEM.getId(src.getItem());
			jsonObject.addProperty(NAME, Registry.ITEM.getId(src.getItem()).toString());
			jsonObject.addProperty(STACK_SIZE, src.getCount());

			if (src.getTag() != null) {
				jsonObject.addProperty(TAG_COMPOUND, src.getTag().toString());
			}

			return jsonObject;
		}

		return JsonNull.INSTANCE;
	}
}*/