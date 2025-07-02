package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;

public class SludgeConvertingDisplay extends FluidConvertingDisplay {
	
	public SludgeConvertingDisplay(RecipeHolder<SludgeConvertingRecipe> recipe) {
		super(recipe);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.SLUDGE_CONVERTING;
	}
	
	@Override
	public ResourceLocation getUnlockIdentifier() {
		return SludgeConvertingRecipe.UNLOCK_IDENTIFIER;
	}
	
}