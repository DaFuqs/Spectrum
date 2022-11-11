package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopBrewingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.entity.effect.StatusEffect;

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
	
}