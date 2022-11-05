package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.REIHelper;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FusionShrineDisplay implements SimpleGridMenuDisplay, GatedRecipeDisplay {
	
	protected boolean secret;
	protected final EntryIngredient fluidInput;
	protected final List<EntryIngredient> craftingInputs;
	protected final List<EntryIngredient> allInputs;
	
	protected final EntryIngredient output;
	protected final float experience;
	protected final int craftingTime;
	protected final Identifier requiredAdvancementIdentifier;
	protected final Optional<Text> description;
	
	public FusionShrineDisplay(@NotNull FusionShrineRecipe recipe) {
		this.secret = recipe.isSecret();
		this.craftingInputs = REIHelper.toEntryIngredients(recipe.getIngredientStacks());
		this.output = EntryIngredients.of(recipe.getOutput());
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		this.fluidInput = EntryIngredients.of(recipe.getFluidInput());
		this.allInputs = new ArrayList<>();
		this.allInputs.addAll(this.craftingInputs);
		this.allInputs.add(this.fluidInput);
		this.description = recipe.getDescription();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}
	
	@Override
	public List<EntryIngredient> getInputEntries() {
		if (this.isUnlocked()) {
			return allInputs;
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<EntryIngredient> getOutputEntries() {
		if (this.isUnlocked() || SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked) {
			return Collections.singletonList(output);
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.FUSION_SHRINE;
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public int getWidth() {
		return 3;
	}
	
	@Override
	public int getHeight() {
		return 3;
	}
	
	public Optional<Text> getDescription() {
		return this.description;
	}
	
	@Override
	public boolean isSecret() {
		return secret;
	}
	
}