package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopReactingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.client.MinecraftClient;

public class PotionWorkshopReactingDisplay extends GatedItemInformationDisplay {
	
	public PotionWorkshopReactingDisplay(PotionWorkshopReactingRecipe recipe) {
		super(recipe);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_REACTING;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, PotionWorkshopRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}