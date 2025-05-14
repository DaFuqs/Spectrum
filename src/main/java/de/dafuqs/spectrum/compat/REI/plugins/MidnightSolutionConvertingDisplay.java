package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;

public class MidnightSolutionConvertingDisplay extends FluidConvertingDisplay {
	
	public MidnightSolutionConvertingDisplay(RecipeHolder<MidnightSolutionConvertingRecipe> recipe) {
		super(recipe);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING;
	}
	
	@Override
	public ResourceLocation getUnlockIdentifier() {
		return MidnightSolutionConvertingRecipe.UNLOCK_IDENTIFIER;
	}
	
}