package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.blocks.present.PresentItem;
import de.dafuqs.spectrum.items.PigmentItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class WrapPresentRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<WrapPresentRecipe> SERIALIZER = new SpecialRecipeSerializer<>(WrapPresentRecipe::new);
	
	public WrapPresentRecipe(Identifier identifier) {
		super(identifier);
	}
	
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean presentItemFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if(itemStack.getItem() instanceof PresentItem) {
					if(presentItemFound || PresentItem.isWrapped(itemStack)) {
						return false;
					}
					presentItemFound = true;
				} else if(!(itemStack.getItem() instanceof PigmentItem)) {
					return false;
				}
			}
		}
		
		return presentItemFound;
	}
	
	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack presentStack = ItemStack.EMPTY;
		Map<DyeColor, Integer> colors = new HashMap<>();
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack stack = craftingInventory.getStack(j);
			if (stack.getItem() instanceof PresentItem) {
				presentStack = stack.copy();
			} else if(stack.getItem() instanceof PigmentItem pigmentItem) {
				DyeColor color = pigmentItem.getColor();
				if(colors.containsKey(color)) {
					colors.put(color, colors.get(color) + 1);
				} else {
					colors.put(color, 1);
				}
			}
		}

		PresentItem.wrap(presentStack, colors);
		return presentStack;
	}
	
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
