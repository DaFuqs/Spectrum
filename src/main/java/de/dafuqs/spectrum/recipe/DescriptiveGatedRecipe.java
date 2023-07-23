package de.dafuqs.spectrum.recipe;

import net.minecraft.item.*;
import net.minecraft.text.*;

public interface DescriptiveGatedRecipe extends GatedRecipe {
	
	Text getDescription();
	
	Item getItem();
	
}
