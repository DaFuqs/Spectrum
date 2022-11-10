package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract class GatedSpectrumDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	protected boolean secret;
	protected final Identifier requiredAdvancementIdentifier;
	
	public GatedSpectrumDisplay(GatedRecipe recipe, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(inputs, outputs);
		this.secret = recipe.isSecret();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public boolean isSecret() {
		return secret;
	}
	
}