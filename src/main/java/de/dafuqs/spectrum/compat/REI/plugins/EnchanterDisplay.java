package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.*;
import me.shedaniel.rei.api.common.entry.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class EnchanterDisplay extends GatedSpectrumDisplay {
	
	// first input is the center, all others around clockwise
	public EnchanterDisplay(@NotNull RecipeEntry<? extends GatedSpectrumRecipe<?>> recipe, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(recipe, inputs, outputs);
	}
	
}