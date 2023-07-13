package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.util.*;

public class MudConvertingDisplay extends FluidConvertingDisplay {
	
	public MudConvertingDisplay(MudConvertingRecipe recipe) {
		super(recipe);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.MUD_CONVERTING;
	}
	
	@Override
	public Identifier getUnlockIdentifier() {
		return MudConvertingRecipe.UNLOCK_IDENTIFIER;
	}
	
}