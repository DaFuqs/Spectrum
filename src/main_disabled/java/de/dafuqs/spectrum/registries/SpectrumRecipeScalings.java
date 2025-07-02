package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.recipe.*;
import net.minecraft.core.*;

public class SpectrumRecipeScalings {
	
	public static void init() {
		register(RecipeScaling.LINEAR);
		register(RecipeScaling.DOUBLING);
		register(RecipeScaling.EXPONENTIAL);
		register(RecipeScaling.INDEXED);
	}
	
	private static void register(RecipeScaling scaling) {
		Registry.register(SpectrumRegistries.RECIPE_SCALING, scaling.getId(), scaling);
	}
}
