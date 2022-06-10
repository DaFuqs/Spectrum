package de.dafuqs.spectrum.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.TranslatableText;

public interface DescriptiveGatedRecipe extends Recipe<Inventory>, GatedRecipe {
	
	TranslatableText getDescription();
	Item getItem();
	
}
