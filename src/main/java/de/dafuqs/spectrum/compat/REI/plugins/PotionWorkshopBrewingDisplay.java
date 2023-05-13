package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.client.*;
import net.minecraft.entity.effect.*;

public class PotionWorkshopBrewingDisplay extends PotionWorkshopRecipeDisplay {
	
	protected final StatusEffect statusEffect;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PotionWorkshopBrewingDisplay(PotionWorkshopBrewingRecipe recipe) {
		super(recipe);
		this.statusEffect = recipe.getStatusEffect();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_BREWING;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, PotionWorkshopRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}