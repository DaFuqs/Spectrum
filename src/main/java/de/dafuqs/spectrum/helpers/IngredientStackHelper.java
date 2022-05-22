package de.dafuqs.spectrum.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.recipe.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientStackHelper {
	
	public static List<IngredientStack> ingredientsFromJson(JsonArray array, int size) {
		List<IngredientStack> ingredients = new ArrayList<>(size);
		int dif = size - array.size();
		for (int i = 0; i < array.size() && i < size; i++) {
			JsonObject object = array.get(i).getAsJsonObject();
			ingredients.add(ingredientFromJson(object));
		}
		if(dif > 0) {
			for (int i = 0; i < dif; i++) {
				ingredients.add(IngredientStack.EMPTY);
			}
		}
		return ingredients;
	}
	
	public static IngredientStack ingredientFromJson(JsonObject json) {
		Ingredient ingredient = Ingredient.fromJson(json);
		int count = json.has("count") ? json.get("count").getAsInt() : 1;
		return IngredientStack.of(ingredient, count);
	}
	
}
