package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class PotionWorkshopCraftingRecipe extends PotionWorkshopRecipe {
	
	protected final int craftingTime;
	protected final ItemStack output;

	public PotionWorkshopCraftingRecipe(Identifier id, String group, Ingredient baseIngredient, boolean consumeBaseIngredient, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, ItemStack output, int craftingTime, Identifier requiredAdvancementIdentifier) {
		super(id, group, baseIngredient, consumeBaseIngredient, ingredient1, ingredient2, ingredient3, requiredAdvancementIdentifier);
		this.craftingTime = craftingTime;
		this.output = output;
	}

	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING_RECIPE_SERIALIZER;
	}

	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return null;
	}
	
}
