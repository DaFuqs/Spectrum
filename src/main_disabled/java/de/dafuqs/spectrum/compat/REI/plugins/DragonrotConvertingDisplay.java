package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;

public class DragonrotConvertingDisplay extends FluidConvertingDisplay {
	
	public DragonrotConvertingDisplay(RecipeHolder<DragonrotConvertingRecipe> recipe) {
		super(recipe);
	}
	
	@Override
	public ResourceLocation getUnlockIdentifier() {
		return DragonrotConvertingRecipe.UNLOCK_IDENTIFIER;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.DRAGONROT_CONVERTING;
	}
	
}