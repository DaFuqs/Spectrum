package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpiritInstillerRecipeDisplay implements SimpleGridMenuDisplay, GatedRecipeDisplay {
	
	protected final List<EntryIngredient> craftingInputs;
	protected final EntryIngredient output;
	protected final float experience;
	protected final int craftingTime;
	protected final Identifier requiredAdvancementIdentifier;

	public SpiritInstillerRecipeDisplay(@NotNull SpiritInstillerRecipe recipe) {
		this.craftingInputs = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));
		this.output = EntryIngredients.of(recipe.getOutput());
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		if(this.isUnlocked()) {
			return craftingInputs;
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		if(this.isUnlocked() || SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked) {
			return Collections.singletonList(output);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.SPIRIT_INSTILLER;
	}

	public boolean isUnlocked() {
		return Support.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 3;
	}

}