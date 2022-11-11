package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GatedSpectrumDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	private final Identifier requiredAdvancementIdentifier;
	private final boolean secret;
	
	public GatedSpectrumDisplay(GatedRecipe recipe, List<EntryIngredient> inputs, ItemStack output) {
		this(recipe, inputs, Collections.singletonList(EntryIngredients.of(output)));
	}
	
	public GatedSpectrumDisplay(GatedRecipe recipe, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(inputs, outputs);
		this.secret = recipe.isSecret();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}
	
	public GatedSpectrumDisplay(Identifier requiredAdvancementIdentifier, boolean secret, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(inputs, outputs);
		this.secret = secret;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
	}
	
	@Override
	public List<EntryIngredient> getInputEntries() {
		if (this.isUnlocked()) {
			return super.getInputEntries();
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<EntryIngredient> getOutputEntries() {
		if (this.isUnlocked() || SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked) {
			return super.getOutputEntries();
		} else {
			return new ArrayList<>();
		}
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public boolean isSecret() {
		return secret;
	}
	
}