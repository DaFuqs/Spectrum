package de.dafuqs.pigment.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface AccessorShapedRecipe {

	@Invoker(value = "getIngredients")
	static DefaultedList<Ingredient> invokeGetIngredients(String[] pattern, Map<String, Ingredient> key, int width, int height) {
		throw new AssertionError();
	}

	@Invoker(value = "combinePattern")
	static String[] invokeCombinePattern(String... lines) {
		throw new AssertionError();
	}

	@Invoker(value = "getPattern")
	static String[] invokeGetPattern(JsonArray json) {
		throw new AssertionError();
	}

	@Invoker(value = "getComponents")
	static Map<String, Ingredient> invokeGetComponents(JsonObject json) {
		throw new AssertionError();
	}

}