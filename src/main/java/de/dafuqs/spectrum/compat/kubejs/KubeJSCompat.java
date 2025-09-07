package de.dafuqs.spectrum.compat.kubejs;

import com.google.common.collect.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import dev.latvian.mods.kubejs.*;
import dev.latvian.mods.kubejs.recipe.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;

import java.util.*;

public class KubeJSCompat extends KubeJSPlugin {
	@Override
	public void injectRuntimeRecipes(RecipesEventJS event, RecipeManager manager, Map<Identifier, Recipe<?>> recipesByName) {
		recipesByName.putAll(EnchantmentUpgradeRecipeSerializer.enchantmentUpgradeRecipesToInject.stream().collect(ImmutableMap.toImmutableMap(EnchantmentUpgradeRecipe::getId, enchantmentUpgradeRecipe -> enchantmentUpgradeRecipe)));
	}
}
