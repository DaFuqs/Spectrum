package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.energy.color.InkColor;
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
	
	protected boolean secret;
	protected final InkColor color;
	protected final long amount;
	@Nullable protected final Identifier requiredAdvancementIdentifier;
	
	public InkConvertingDisplay(@NotNull InkConvertingRecipe recipe) {
		super(EntryIngredients.ofIngredients(recipe.getIngredients()), List.of());
		this.secret = recipe.isSecret();
		this.color = recipe.getInkColor();
		this.amount = recipe.getInkAmount();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.INK_CONVERTING;
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, InkConvertingRecipe.UNLOCK_IDENTIFIER) && AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public boolean isSecret() {
		return secret;
	}
	
}