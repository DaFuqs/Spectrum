package de.dafuqs.spectrum.recipe;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;

public interface DescriptiveGatedRecipe extends Recipe<Inventory>, GatedRecipe {
	
	Text getDescription();
	
	Item getItem();
	
}
