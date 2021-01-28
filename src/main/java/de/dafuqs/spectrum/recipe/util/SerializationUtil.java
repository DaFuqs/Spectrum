/*package de.dafuqs.spectrum.recipe.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SerializationUtil {

	public static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.enableComplexMapKeySerialization()
			.registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
			.create();

	//Same as above, just without pretty printing
	public static final Gson GSON_FLAT = new GsonBuilder()
			.enableComplexMapKeySerialization()
			.registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
			.create();


	public static Stream<JsonElement> stream(JsonArray array) {
		return IntStream.range(0, array.size())
				.mapToObj(array::get);
	}

	public static JsonArray asArray(List<JsonElement> elements) {
		JsonArray array = new JsonArray();
		elements.forEach(array::add);
		return array;
	}

}*/