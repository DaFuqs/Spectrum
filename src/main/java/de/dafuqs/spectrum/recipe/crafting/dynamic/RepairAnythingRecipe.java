package de.dafuqs.spectrum.recipe.crafting.dynamic;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class RepairAnythingRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<RepairAnythingRecipe> SERIALIZER = new SpecialRecipeSerializer<>(RepairAnythingRecipe::new);
	
	private static final Ingredient MOONSTRUCK_NECTAR = Ingredient.ofItems(SpectrumItems.MOONSTRUCK_NECTAR);
	
	public RepairAnythingRecipe(Identifier identifier, CraftingRecipeCategory category) {
		super(identifier, category);
	}
	
	@Override
	public boolean matches(RecipeInputInventory craftingInventory, World world) {
		boolean nectarFound = false;
		boolean itemFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
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
	
	@Override
	public ItemStack craft(RecipeInputInventory craftingInventory, DynamicRegistryManager drm) {
		ItemStack itemStack = ItemStack.EMPTY;
		for (int j = 0; j < craftingInventory.size(); ++j) {
			itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty() && !MOONSTRUCK_NECTAR.test(itemStack)) {
				break;
			}
		}
		
		if (itemStack.isDamageable() && itemStack.isDamaged() && !itemStack.isIn(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED)) {
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
	
	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
