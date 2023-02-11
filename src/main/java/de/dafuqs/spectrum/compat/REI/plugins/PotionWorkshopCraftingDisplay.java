package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.Ingredient;

public class PotionWorkshopCraftingDisplay extends PotionWorkshopRecipeDisplay {
	
	protected final Ingredient baseIngredient;
	protected final boolean consumeBaseIngredient;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PotionWorkshopCraftingDisplay(PotionWorkshopCraftingRecipe recipe) {
		super(recipe);
		this.baseIngredient = recipe.getBaseIngredient();
		this.consumeBaseIngredient = recipe.consumesBaseIngredient();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_CRAFTING;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, PotionWorkshopRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}