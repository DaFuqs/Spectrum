package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.GatedSpectrumDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.fluid_converting.LiquidCrystalConvertingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;

import java.util.Collections;

public class LiquidCrystalConvertingDisplay extends GatedSpectrumDisplay {
	
	public LiquidCrystalConvertingDisplay(LiquidCrystalConvertingRecipe recipe) {
		super(recipe, Collections.singletonList(EntryIngredients.ofIngredient(recipe.getIngredients().get(0))), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
	}
	
	public final EntryIngredient getIn() {
		return getInputEntries().get(0);
	}
	
	public final EntryIngredient getOut() {
		return getOutputEntries().get(0);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.LIQUID_CRYSTAL_CONVERTING;
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}