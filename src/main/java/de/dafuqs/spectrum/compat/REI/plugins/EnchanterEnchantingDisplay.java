package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.registry.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class EnchanterEnchantingDisplay extends EnchanterDisplay {
	
	protected final int requiredExperience;
	protected final int craftingTime;
	
	// first input is the center, all others around clockwise
	public EnchanterEnchantingDisplay(@NotNull EnchanterRecipe recipe) {
		super(recipe, buildIngredients(recipe), recipe.getOutput(DynamicRegistryManager.EMPTY));
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
		return SpectrumPlugins.ENCHANTER_CRAFTING;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, EnchanterRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}