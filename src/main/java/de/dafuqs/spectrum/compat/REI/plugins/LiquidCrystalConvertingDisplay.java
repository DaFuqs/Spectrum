package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;

public class LiquidCrystalConvertingDisplay extends FluidConvertingDisplay {
	
	public LiquidCrystalConvertingDisplay(RecipeHolder<LiquidCrystalConvertingRecipe> recipe) {
		super(recipe);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.LIQUID_CRYSTAL_CONVERTING;
	}
	
	@Override
	public ResourceLocation getUnlockIdentifier() {
		return LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER;
	}
	
}