package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.ink_converting.InkConvertingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InkConvertingDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	protected final InkColor color;
	protected final long amount;
	
	@Nullable
	protected final Identifier requiredAdvancementIdentifier;
	
	public InkConvertingDisplay(@NotNull InkConvertingRecipe recipe) {
		super(EntryIngredients.ofIngredients(recipe.getIngredients()), List.of());
		this.color = recipe.getInkColor();
		this.amount = recipe.getInkAmount();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.INK_CONVERTING;
	}
	
	public boolean isUnlocked() {
		return Support.hasAdvancement(MinecraftClient.getInstance().player, InkConvertingRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER) && Support.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
}