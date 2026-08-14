package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public class PotionWorkshopReactingEmiRecipe extends EmiInfoRecipe {
	private final DescriptiveGatedRecipe<?> recipe;
	
	public <T extends DescriptiveGatedRecipe<?>> PotionWorkshopReactingEmiRecipe(RecipeHolder<T> entry) {
		super(List.of(EmiStack.of(entry.value().getItem())), List.of(entry.value().getDescription()), entry.id());
		this.recipe = entry.value();
	}
	
	public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, PotionWorkshopRecipe.UNLOCK_IDENTIFIER) && AdvancementHelper.hasAdvancement(client.player, recipe.getRequiredAdvancement().orElse(null));
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
			widgets.addText(SpectrumEmiRecipe.HIDDEN_LINE_1, getDisplayWidth() / 2, getDisplayHeight() / 2 - 8, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
			widgets.addText(SpectrumEmiRecipe.HIDDEN_LINE_2, getDisplayWidth() / 2, getDisplayHeight() / 2 + 2, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
		} else {
			super.addWidgets(widgets);
		}
	}
}
