package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.cinderhearth.CinderhearthRecipe;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CinderhearthDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	protected final Identifier requiredAdvancementIdentifier;
	protected final float experience;
	protected final int craftingTime;
	protected final List<Pair<ItemStack, Float>> outputsWithChance;
	
	public CinderhearthDisplay(@NotNull CinderhearthRecipe recipe) {
		super(Collections.singletonList(EntryIngredients.ofIngredient(recipe.getIngredients().get(0))), List.of(EntryIngredients.ofItemStacks(recipe.getPossibleOutputs())));
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
		this.outputsWithChance = recipe.getOutputsWithChance();
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
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
		return SpectrumPlugins.CINDERHEARTH;
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
}