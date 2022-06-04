package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.midnight_solution_converting.MidnightSolutionConvertingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class MidnightSolutionConvertingDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	public static final Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/create_midnight_aberration");
	
	public <T extends Recipe<?>> MidnightSolutionConvertingDisplay(MidnightSolutionConvertingRecipe recipe) {
		this(Collections.singletonList(EntryIngredients.ofIngredient(recipe.getIngredients().get(0))), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
	}

	public MidnightSolutionConvertingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(inputs, outputs);
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
	
	public boolean isUnlocked() {
		return Support.hasAdvancement(MinecraftClient.getInstance().player, UNLOCK_ADVANCEMENT_IDENTIFIER);
	}
	
	public static Serializer<MidnightSolutionConvertingDisplay> serializer() {
		return Serializer.ofSimpleRecipeLess(MidnightSolutionConvertingDisplay::new);
	}
	
}