package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ColorBondingRibbonRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ColorBondingRibbonRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ColorBondingRibbonRecipe::new);
	
	public ColorBondingRibbonRecipe(Identifier identifier) {
		super(identifier);
	}
	
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean bondingRibbonFound = false;
		boolean dyeFound = false;
		
		for (int i = 0; i < craftingInventory.size(); ++i) {
			ItemStack itemStack = craftingInventory.getStack(i);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof BondingRibbonItem) {
					if (!itemStack.hasCustomName()) {
						return false;
					}
					if (bondingRibbonFound) {
						return false;
					} else {
						bondingRibbonFound = true;
					}
				} else if (itemStack.getItem() instanceof DyeItem) {
					if (dyeFound) {
						return false;
					} else {
						dyeFound = true;
					}
				} else {
					return false;
				}
			}
		}
		
		return bondingRibbonFound && dyeFound;
	}
	
	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack bondingRibbon = null;
		DyeItem dye = null;
		
		
		for (int i = 0; i < craftingInventory.size(); ++i) {
			ItemStack stack = craftingInventory.getStack(i);
			if (stack.getItem() instanceof BondingRibbonItem) {
				bondingRibbon = stack;
			}
			if (stack.getItem() instanceof DyeItem dyeItem) {
				dye = dyeItem;
			}
		}
		
		if (bondingRibbon == null || dye == null) {
			return ItemStack.EMPTY;
		}
		
		bondingRibbon = bondingRibbon.copy();
		bondingRibbon.setCount(1);
		
		Text text = bondingRibbon.getName();
		if (text instanceof MutableText mutableText) {
			TextColor newColor = TextColor.fromRgb(ColorHelper.getInt(dye.getColor()));
			Text newName = mutableText.setStyle(mutableText.getStyle().withColor(newColor));
			bondingRibbon.setCustomName(newName);
		}
		
		return bondingRibbon;
	}
	
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
