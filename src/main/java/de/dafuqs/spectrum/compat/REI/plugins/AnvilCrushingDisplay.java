package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class AnvilCrushingDisplay implements Display, GatedRecipeDisplay {
	
	protected boolean secret;
	public final float experience;
	public final float crushedItemsPerPointOfDamage;
	private final List<EntryIngredient> inputs;
	private final EntryIngredient output;
	protected final Identifier requiredAdvancementIdentifier;
	
	public AnvilCrushingDisplay(AnvilCrushingRecipe recipe) {
		this.secret = recipe.isSecret();
		this.inputs = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).toList();
		this.output = EntryIngredients.of(recipe.getOutput());
		
		this.experience = recipe.getExperience();
		this.crushedItemsPerPointOfDamage = recipe.getCrushedItemsPerPointOfDamage();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}
	
	@Override
	public List<EntryIngredient> getInputEntries() {
		return inputs;
	}
	
	@Override
	public List<EntryIngredient> getOutputEntries() {
		return Collections.singletonList(output);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.ANVIL_CRUSHING;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public boolean isSecret() {
		return secret;
	}
	
}