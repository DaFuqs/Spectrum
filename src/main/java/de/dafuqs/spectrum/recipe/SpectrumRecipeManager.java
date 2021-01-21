package de.dafuqs.spectrum.recipe;

import com.google.common.collect.ImmutableMap;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class SpectrumRecipeManager {

	private static final Map<Identifier, SpectrumRecipeType<?>> recipeTypes = new HashMap<>();
	private Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes = ImmutableMap.of();

	public static <R extends SpectrumRecipe> RecipeType<R> newRecipeType(BiFunction<SpectrumRecipeType<R>, Identifier, R> recipeFunction, Identifier name) {
		if (recipeTypes.containsKey(name)) {
			throw new RuntimeException("Recipe type with this name already registered");
		}
		SpectrumRecipeType<R> type = new SpectrumRecipeType<>(recipeFunction, name);
		recipeTypes.put(name, type);

		Registry.register(Registry.RECIPE_SERIALIZER, name, (RecipeSerializer<?>) type);

		return type;
	}

	public static SpectrumRecipeType<?> getRecipeType(Identifier name) {
		if (!recipeTypes.containsKey(name)) {
			throw new RuntimeException("Recipe type " + name + " not found");
		}
		return recipeTypes.get(name);
	}

	/*public static List<SpectrumRecipeType> getRecipeTypes(String namespace) {
		return recipeTypes.values().stream().filter(recipeType -> recipeType.getName().getNamespace().equals(namespace)).collect(Collectors.toList());
	}

	public <C extends Inventory, T extends Recipe<C>> Optional<T> getFirstMatch(RecipeType<T> type, C inventory, World world) {
		return this.getAllOfType(type).values().stream().flatMap((recipe) -> {
			return Util.stream(type.get(recipe, world, inventory));
		}).findFirst();
	}

	private <C extends Inventory, T extends Recipe<C>> Map<Identifier, Recipe<C>> getAllOfType(RecipeType<T> type) {
		return (Map)this.recipes.getOrDefault(type, Collections.emptyMap());
	}*/
	
}