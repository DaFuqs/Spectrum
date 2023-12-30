package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.spawner.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.item.*;

import java.util.*;

public class SpiritInstillingEmiRecipeGated extends GatedSpectrumEmiRecipe<SpiritInstillerRecipe> {
	
	public SpiritInstillingEmiRecipeGated(SpiritInstillerRecipe recipe) {
		super(SpectrumEmiRecipeCategories.SPIRIT_INSTILLER, SpiritInstillerRecipe.UNLOCK_IDENTIFIER, recipe, 116, 48);
		inputs = recipe.getIngredientStacks().stream().map(s -> EmiIngredient.of(s.getStacks().stream().map(EmiStack::of).toList())).toList();
		
		if (recipe instanceof SpawnerChangeRecipe spawnerChangeRecipe) {
			ItemStack outputStack = recipe.getOutput(getRegistryManager());
			LoreHelper.setLore(outputStack, spawnerChangeRecipe.getOutputLoreText());
			outputs = List.of(EmiStack.of(outputStack));
		}
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(inputs.get(SpiritInstillerRecipe.FIRST_INGREDIENT), 0, 0);
		widgets.addSlot(inputs.get(SpiritInstillerRecipe.CENTER_INGREDIENT), 20, 0);
		widgets.addSlot(inputs.get(SpiritInstillerRecipe.SECOND_INGREDIENT), 40, 0);
		
		widgets.addSlot(EmiStack.of(SpectrumBlocks.ITEM_BOWL_CALCITE), 0, 17).drawBack(false);
		widgets.addSlot(EmiStack.of(SpectrumBlocks.SPIRIT_INSTILLER), 20, 17).drawBack(false);
		widgets.addSlot(EmiStack.of(SpectrumBlocks.ITEM_BOWL_CALCITE), 40, 17).drawBack(false);
		
		if (!outputs.isEmpty()) {
			widgets.addSlot(outputs.get(0), 90, 4).large(true).recipeContext(this);
		}
		
		widgets.addFillingArrow(60, 9, recipe.getCraftingTime() * 50);

		widgets.addText(getCraftingTimeText(recipe.getCraftingTime(), recipe.getExperience()), 0, 39, 0x3f3f3f, false);
	}
}
