package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.GatedSpectrumDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.items.magic_items.KnowledgeGemItem;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnchanterDisplay extends GatedSpectrumDisplay {
	
	protected final int requiredExperience;
	protected final int craftingTime;
	
	// first input is the center, all others around clockwise
	public EnchanterDisplay(@NotNull EnchanterRecipe recipe) {
		super(recipe, buildIngredients(recipe), recipe.getOutput());
		this.requiredExperience = recipe.getRequiredExperience();
		this.craftingTime = recipe.getCraftingTime();
	}
	
	private static List<EntryIngredient> buildIngredients(EnchanterRecipe recipe) {
		List<EntryIngredient> inputs = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));
		inputs.add(EntryIngredients.of(KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true)));
		return inputs;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.ENCHANTER;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, EnchanterRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}