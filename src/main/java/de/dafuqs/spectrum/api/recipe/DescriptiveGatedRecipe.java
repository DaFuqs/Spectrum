package de.dafuqs.spectrum.api.recipe;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.text.*;

public interface DescriptiveGatedRecipe<C extends Inventory> extends GatedRecipe<C> {
	
	Text getDescription();
	
	Item getItem();
	
}
