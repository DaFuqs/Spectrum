package de.dafuqs.pigment.accessor;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(RecipeManager.class)
public interface AccessorRecipeManager {

	@Invoker(value = "getAllOfType")
	<C extends Inventory, T extends Recipe<C>> Map<Identifier, Recipe<C>> getAll(RecipeType<T> type);
}