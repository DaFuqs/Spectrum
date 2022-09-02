package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.GatedSpectrumDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.recipe.ink_converting.InkConvertingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InkConvertingDisplay extends GatedSpectrumDisplay {
	
	protected final InkColor color;
	protected final long amount;
	
	public InkConvertingDisplay(@NotNull InkConvertingRecipe recipe) {
		super(recipe, EntryIngredients.ofIngredients(recipe.getIngredients()), List.of());
		this.color = recipe.getInkColor();
		this.amount = recipe.getInkAmount();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.INK_CONVERTING;
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, InkConvertingRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}