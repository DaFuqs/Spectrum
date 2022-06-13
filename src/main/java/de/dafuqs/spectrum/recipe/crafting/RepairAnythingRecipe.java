package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.registries.SpectrumItemTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RepairAnythingRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<RepairAnythingRecipe> SERIALIZER = new SpecialRecipeSerializer<>(RepairAnythingRecipe::new);
	
	private static final Ingredient MOONSTRUCK_NECTAR = Ingredient.ofItems(SpectrumItems.MOONSTRUCK_NECTAR);
	
	public RepairAnythingRecipe(Identifier identifier) {
		super(identifier);
	}
	
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean nectarFound = false;
		boolean itemFound = false;
		
		for(int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if (MOONSTRUCK_NECTAR.test(itemStack)) {
					if (nectarFound) {
						return false;
					}
					nectarFound = true;
				} else if (itemStack.isDamageable() && itemStack.isDamaged() && !itemStack.isIn(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED)) {
					if (itemFound) {
						return false;
					}
					itemFound = true;
				}
			}
		}
		
		return nectarFound && itemFound;
	}
	
	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack itemStack = ItemStack.EMPTY;
		for(int j = 0; j < craftingInventory.size(); ++j) {
			itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty() && !MOONSTRUCK_NECTAR.test(itemStack)) {
				break;
			}
		}
		
		if(itemStack.isDamageable() && itemStack.isDamaged() && !itemStack.isIn(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED)) {
			ItemStack returnStack = itemStack.copy();
			int damage = returnStack.getDamage();
			int maxDamage = returnStack.getMaxDamage();
			
			int newDamage = Math.max(0, damage - maxDamage / 3);
			returnStack.setDamage(newDamage);
			return returnStack;
		} else {
			return ItemStack.EMPTY;
		}
	}
	
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
