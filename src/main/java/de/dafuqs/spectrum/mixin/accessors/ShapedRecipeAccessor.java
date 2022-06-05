package de.dafuqs.spectrum.mixin.accessors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {
	
	@Invoker(value = "createPatternMatrix")
	static DefaultedList<Ingredient> invokeCreatePatternMatrix(String[] pattern, Map<String, Ingredient> key, int width, int height) {
		throw new AssertionError();
	}
	
	@Invoker(value = "removePadding")
	static String[] invokeRemovePadding(String... lines) {
		throw new AssertionError();
	}
	
	@Invoker(value = "getPattern")
	static String[] invokeGetPattern(JsonArray json) {
		throw new AssertionError();
	}
	
	@Invoker(value = "readSymbols")
	static Map<String, Ingredient> invokeReadSymbols(JsonObject json) {
		throw new AssertionError();
	}
	
}