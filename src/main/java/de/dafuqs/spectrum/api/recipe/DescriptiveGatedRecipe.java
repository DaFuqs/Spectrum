package de.dafuqs.spectrum.api.recipe;

import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

public interface DescriptiveGatedRecipe<C extends RecipeInput> extends GatedRecipe<C> {
	
	Component getDescription();
	
	Item getItem();
	
}
