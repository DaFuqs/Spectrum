package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.client.*;

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