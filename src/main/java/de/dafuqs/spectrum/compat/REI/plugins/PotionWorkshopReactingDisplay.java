package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopReactingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import static de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopRecipe.UNLOCK_POTION_WORKSHOP_ADVANCEMENT_IDENTIFIER;

public class PotionWorkshopReactingDisplay extends GatedItemInformationDisplay {
	
	public PotionWorkshopReactingDisplay(PotionWorkshopReactingRecipe recipe) {
		super(recipe);
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isUnlockedClient() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, UNLOCK_POTION_WORKSHOP_ADVANCEMENT_IDENTIFIER) && super.isUnlockedClient();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_REACTING;
	}
	
}