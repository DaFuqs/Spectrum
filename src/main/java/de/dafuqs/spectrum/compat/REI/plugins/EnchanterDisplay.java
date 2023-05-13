package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.*;
import me.shedaniel.rei.api.common.entry.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class EnchanterDisplay extends GatedSpectrumDisplay {
	
	// first input is the center, all others around clockwise
	public EnchanterDisplay(@NotNull GatedSpectrumRecipe recipe, List<EntryIngredient> inputs, ItemStack output) {
		super(recipe, inputs, output);
	}
	
}