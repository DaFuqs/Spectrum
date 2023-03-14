package de.dafuqs.spectrum.compat.emi.recipes;

import java.util.List;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.emi.SpectrumRecipeCategories;
import de.dafuqs.spectrum.recipe.DescriptiveGatedRecipe;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget.Alignment;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class GatedInfoEmiRecipe extends EmiInfoRecipe {
	private static final Text HIDDEN_LINE_1 = Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1");
	private static final Text HIDDEN_LINE_2 = Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2");
	private final DescriptiveGatedRecipe recipe;

	public GatedInfoEmiRecipe(DescriptiveGatedRecipe recipe) {
		super(List.of(EmiStack.of(recipe.getItem())), List.of(recipe.getDescription()), recipe.getId());
		this.recipe = recipe;
	}

	public boolean isUnlocked() {
		MinecraftClient client = MinecraftClient.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, recipe.getRequiredAdvancementIdentifier());
	}
	
	@Override
	public EmiRecipeCategory getCategory() {
		return SpectrumRecipeCategories.POTION_WORKSHOP_REACTING;
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
			widgets.addText(HIDDEN_LINE_1, getDisplayWidth() / 2, getDisplayHeight() / 2 - 8, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
			widgets.addText(HIDDEN_LINE_2, getDisplayWidth() / 2, getDisplayHeight() / 2 + 2, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
		} else {
			super.addWidgets(widgets);
		}
	}
}
