package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrystallarieumDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	protected final Identifier requiredAdvancementIdentifier;
	
	public CrystallarieumDisplay(@NotNull CrystallarieumRecipe recipe) {
		super(Collections.singletonList(EntryIngredients.ofIngredient(recipe.getIngredientStack())), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));

		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}
	
	@Override
	public List<EntryIngredient> getInputEntries() {
		if (this.isUnlocked()) {
			return super.getInputEntries();
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<EntryIngredient> getOutputEntries() {
		if (this.isUnlocked() || SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked) {
			return super.getOutputEntries();
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.CRYSTALLARIEUM;
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
}