package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.registry.*;
import net.minecraft.text.*;

import java.util.*;

public class FusionShrineEmiRecipeGated extends GatedSpectrumEmiRecipe<FusionShrineRecipe> {
	private final List<OrderedText> texts;
	
	public FusionShrineEmiRecipeGated(FusionShrineRecipe recipe) {
		super(SpectrumEmiRecipeCategories.FUSION_SHRINE, FusionShrineRecipe.UNLOCK_IDENTIFIER, recipe, 138, 60);
		if (recipe.getDescription().isPresent()) {
			texts = MinecraftClient.getInstance().textRenderer.wrapLines(recipe.getDescription().get(), width);
		} else {
			texts = List.of();
		}
		input = new ArrayList<>();
		input.add(EmiIngredient.of(List.of(EmiStack.of(recipe.getFluidInput()))));
		input.addAll(recipe.getIngredientStacks().stream().map(s -> EmiIngredient.of(s.getStacks().stream().map(EmiStack::of).toList())).toList());
		output = List.of(EmiStack.of(recipe.getOutput(DynamicRegistryManager.EMPTY)));
	}

	@Override
	public int getDisplayHeight() {
		if (isUnlocked()) {
			return height + texts.size() * 10;
		} else {
			return super.getDisplayHeight();
		}
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		// shrine + fluid
		if (!input.get(0).isEmpty()) {
			widgets.addSlot(EmiStack.of(SpectrumBlocks.FUSION_SHRINE_BASALT), 10, 25).drawBack(false);
			widgets.addSlot(input.get(0), 30, 25);
		} else {
			widgets.addSlot(EmiStack.of(SpectrumBlocks.FUSION_SHRINE_BASALT), 20, 25).drawBack(false);
		}

		// input slots
		int startX = Math.max(-20, 20 - input.size() * 10);
		for (int i = 1; i < input.size(); i++) {
			widgets.addSlot(input.get(i), startX + i * 20, 0);
		}
		
		widgets.addSlot(output.get(0), 90, 20).large(true).recipeContext(this);
		
		widgets.addFillingArrow(60, 25, recipe.getCraftingTime() * 50);

		for (int i = 0; i < texts.size(); i++) {
			widgets.addText(texts.get(i), 0, 50 + i * 10, 0x3f3f3f, false);
		}

		widgets.addText(getCraftingTimeText(recipe.getCraftingTime(), recipe.getExperience()), width / 2, 50 + texts.size() * 10, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}
}
