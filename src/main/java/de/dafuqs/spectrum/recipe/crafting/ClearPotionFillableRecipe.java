package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.items.PotionFillable;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ClearPotionFillableRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ClearPotionFillableRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearPotionFillableRecipe::new);
	
	public ClearPotionFillableRecipe(Identifier identifier) {
		super(identifier);
	}
	
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean potionFillableFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof PotionFillable potionFillable) {
					if (potionFillable.isAtLeastPartiallyFilled(itemStack)) {
						potionFillableFound = true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		
		return potionFillableFound;
	}
	
	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack itemStack;
		for (int j = 0; j < craftingInventory.size(); ++j) {
			itemStack = craftingInventory.getStack(j).copy();
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof PotionFillable potionFillable) {
				potionFillable.removeEffects(itemStack);
				return itemStack;
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	public boolean fits(int width, int height) {
		return width * height >= 1;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
