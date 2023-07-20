package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.*;
import net.minecraft.registry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ColorBondingRibbonRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ColorBondingRibbonRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ColorBondingRibbonRecipe::new);
	
	public ColorBondingRibbonRecipe(Identifier identifier, CraftingRecipeCategory category) {
		super(identifier, category);
	}
	
	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean ribbonFound = false;
		boolean pigmentFound = false;
		
		for (int i = 0; i < craftingInventory.size(); ++i) {
			ItemStack itemStack = craftingInventory.getStack(i);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof EverpromiseRibbonItem) {
					if (!itemStack.hasCustomName()) {
						return false;
					}
					if (ribbonFound) {
						return false;
					} else {
						ribbonFound = true;
					}
				} else if (itemStack.getItem() instanceof PigmentItem) {
					if (pigmentFound) {
						return false;
					} else {
						pigmentFound = true;
					}
				} else {
					return false;
				}
			}
		}
		
		return ribbonFound && pigmentFound;
	}
	
	@Override
	public ItemStack craft(CraftingInventory craftingInventory, DynamicRegistryManager drm) {
		ItemStack ribbon = null;
		PigmentItem pigment = null;
		
		
		for (int i = 0; i < craftingInventory.size(); ++i) {
			ItemStack stack = craftingInventory.getStack(i);
			if (stack.getItem() instanceof EverpromiseRibbonItem) {
				ribbon = stack;
			}
			if (stack.getItem() instanceof PigmentItem pigmentItem) {
				pigment = pigmentItem;
			}
		}
		
		if (ribbon == null || pigment == null) {
			return ItemStack.EMPTY;
		}
		
		ribbon = ribbon.copy();
		ribbon.setCount(1);
		
		Text text = ribbon.getName();
		if (text instanceof MutableText mutableText) {
			TextColor newColor = TextColor.fromRgb(ColorHelper.getInt(pigment.getColor()));
			Text newName = mutableText.setStyle(mutableText.getStyle().withColor(newColor));
			ribbon.setCustomName(newName);
		}
		
		return ribbon;
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
