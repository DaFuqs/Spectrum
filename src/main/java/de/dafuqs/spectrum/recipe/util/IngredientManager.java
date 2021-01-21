package de.dafuqs.spectrum.recipe.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.dafuqs.spectrum.recipe.ingredient.SpectrumIngredient;
import de.dafuqs.spectrum.recipe.ingredient.StackIngredient;
import de.dafuqs.spectrum.recipe.ingredient.TagIngredient;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.HashMap;
import java.util.function.Function;

public class IngredientManager {

	public static final Identifier STACK_RECIPE_TYPE = new Identifier("stack");
	public static final Identifier TAG_RECIPE_TYPE = new Identifier("tag");

	private static final HashMap<Identifier, Function<JsonObject, SpectrumIngredient>> recipeTypes = new HashMap<>();

	public static void setup() {
		recipeTypes.put(STACK_RECIPE_TYPE, StackIngredient::deserialize);
		recipeTypes.put(TAG_RECIPE_TYPE, TagIngredient::deserialize);
	}

	public static SpectrumIngredient deserialize(@Nullable JsonElement jsonElement) {
		if (jsonElement == null || !jsonElement.isJsonObject()) {
			throw new JsonParseException("ingredient must be a json object");
		}

		JsonObject json = jsonElement.getAsJsonObject();

		Identifier recipeTypeIdentifier = STACK_RECIPE_TYPE;
		// vanilla item tags
		if (json.has("tag")) {
			recipeTypeIdentifier = TAG_RECIPE_TYPE;
		}
		// default vanilla
		if (json.has("type")) {
			recipeTypeIdentifier = new Identifier(JsonHelper.getString(json, "type"));
		}

		Function<JsonObject, SpectrumIngredient> recipeTypeFunction = recipeTypes.get(recipeTypeIdentifier);
		if (recipeTypeFunction == null) {
			throw new JsonParseException("No recipe type found for function " + recipeTypeIdentifier.toString());
		}
		return recipeTypeFunction.apply(json);
	}

}