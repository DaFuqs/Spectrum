package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.GatedSpectrumDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.fluid_converting.MidnightSolutionConvertingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;

import java.util.Collections;

public class MidnightSolutionConvertingDisplay extends GatedSpectrumDisplay {
	
	public MidnightSolutionConvertingDisplay(MidnightSolutionConvertingRecipe recipe) {
		super(recipe, Collections.singletonList(EntryIngredients.ofIngredient(recipe.getIngredients().get(0))), recipe.getOutput());
	}
	
	public final EntryIngredient getIn() {
		return getInputEntries().get(0);
	}
	
	public final EntryIngredient getOut() {
		return getOutputEntries().get(0);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, MidnightSolutionConvertingRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}