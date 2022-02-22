package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class PotionWorkshopCraftingRecipe extends PotionWorkshopRecipe {
	
	protected final Ingredient baseIngredient;
	protected final boolean consumeBaseIngredient;
	protected final ItemStack output;
	
	
	public PotionWorkshopCraftingRecipe(Identifier id, String group, Ingredient baseIngredient, boolean consumeBaseIngredient, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, ItemStack output, int craftingTime, Identifier requiredAdvancementIdentifier) {
		super(id, group, craftingTime, ingredient1, ingredient2, ingredient3, requiredAdvancementIdentifier);
		this.output = output;
		this.baseIngredient = baseIngredient;
		this.consumeBaseIngredient = consumeBaseIngredient;
	}
	
	public Ingredient getBaseIngredient() {
		return baseIngredient;
	}
	
	public boolean consumesBaseIngredient() {
		return consumeBaseIngredient;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING_RECIPE_SERIALIZER;
	}

	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING;
	}
	
	@Override
	public boolean usesReagents() {
		return false;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return null;
	}
	
	@Override
	public boolean isValidBaseIngredient(ItemStack itemStack) {
		return baseIngredient.test(itemStack);
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.baseIngredient);
		defaultedList.add(this.ingredient1);
		defaultedList.add(this.ingredient2);
		defaultedList.add(this.ingredient3);
		return defaultedList;
	}
	
	@Override
	public ItemStack getOutput() {
		return output;
	}
	
	@Override
	public int getMinOutputCount(ItemStack itemStack) {
		return 1;
	}
	
}
