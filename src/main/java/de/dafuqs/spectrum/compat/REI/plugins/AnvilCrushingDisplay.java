package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.registry.*;

import java.util.*;

public class AnvilCrushingDisplay extends GatedSpectrumDisplay {
	public final float experience;
	public final float crushedItemsPerPointOfDamage;
	
	public AnvilCrushingDisplay(AnvilCrushingRecipe recipe) {
		super(recipe, recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).toList(), Collections.singletonList(EntryIngredients.of(recipe.getOutput(DynamicRegistryManager.EMPTY))));
		this.experience = recipe.getExperience();
		this.crushedItemsPerPointOfDamage = recipe.getCrushedItemsPerPointOfDamage();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.ANVIL_CRUSHING;
	}
	
}