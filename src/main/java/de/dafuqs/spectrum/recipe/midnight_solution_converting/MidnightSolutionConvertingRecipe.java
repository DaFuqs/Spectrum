package de.dafuqs.spectrum.recipe.midnight_solution_converting;

import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MidnightSolutionConvertingRecipe implements Recipe<Inventory> {

	protected final Identifier id;
	protected final Ingredient inputIngredient;
	protected final ItemStack outputItemStack;
	
	// these can be blacklisted in solution logic for better performance
	public static final List<Item> ALL_OUTPUTS = new ArrayList<>();

	public MidnightSolutionConvertingRecipe(Identifier id, Ingredient inputIngredient, ItemStack outputItemStack) {
		this.id = id;
		this.inputIngredient = inputIngredient;
		this.outputItemStack = outputItemStack;
		
		for(ItemStack itemStack : inputIngredient.getMatchingStacks()) {
			ALL_OUTPUTS.add(itemStack.getItem());
		}
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		return this.inputIngredient.test(inv.getStack(0));
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return null;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return outputItemStack.copy();
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET);
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING_RECIPE;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient);
		return defaultedList;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof MidnightSolutionConvertingRecipe) {
			return ((MidnightSolutionConvertingRecipe) object).getId().equals(this.getId());
		}
		return false;
	}

}
