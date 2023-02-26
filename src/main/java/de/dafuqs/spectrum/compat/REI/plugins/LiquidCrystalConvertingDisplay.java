package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.util.*;

public class LiquidCrystalConvertingDisplay extends FluidConvertingDisplay {

	public LiquidCrystalConvertingDisplay(LiquidCrystalConvertingRecipe recipe) {
		super(recipe);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.LIQUID_CRYSTAL_CONVERTING;
	}

	@Override
	public Identifier getUnlockIdentifier() {
		return LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER;
	}

}