package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;

import java.util.*;

public class PotionWorkshopReactingEmiRecipe extends EmiInfoRecipe {
	private final DescriptiveGatedRecipe recipe;
	
	public PotionWorkshopReactingEmiRecipe(DescriptiveGatedRecipe recipe) {
		super(List.of(EmiStack.of(recipe.getItem())), List.of(recipe.getDescription()), recipe.getId());
		this.recipe = recipe;
	}
	
	public boolean isUnlocked() {
		MinecraftClient client = MinecraftClient.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, recipe.getRequiredAdvancementIdentifier());
	}
	
	@Override
	public EmiRecipeCategory getCategory() {
		return SpectrumEmiRecipeCategories.POTION_WORKSHOP_REACTING;
	}

	@Override
	public int getDisplayHeight() {
		if (isUnlocked()) {
			return super.getDisplayHeight();
		} else {
			return 32;
		}
	}
	
	@Override
	public void addWidgets(WidgetHolder widgets) {
		if (!isUnlocked()) {
			widgets.addText(SpectrumBaseEmiRecipe.HIDDEN_LINE_1, getDisplayWidth() / 2, getDisplayHeight() / 2 - 8, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
			widgets.addText(SpectrumBaseEmiRecipe.HIDDEN_LINE_2, getDisplayWidth() / 2, getDisplayHeight() / 2 + 2, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
		} else {
			super.addWidgets(widgets);
		}
	}
}
