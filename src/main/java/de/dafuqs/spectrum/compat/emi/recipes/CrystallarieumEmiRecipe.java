package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;
import java.util.stream.*;

public class CrystallarieumEmiRecipe extends SpectrumEmiRecipe<CrystallarieumRecipe> {
	private final static Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/crystallarieum.png");

	public CrystallarieumEmiRecipe(CrystallarieumRecipe recipe) {
		super(SpectrumEmiRecipeCategories.CRYSTALLARIEUM, CrystallarieumRecipe.UNLOCK_IDENTIFIER, recipe, 124, 100);
		input = List.of(
				EmiIngredient.of(recipe.getIngredientStack()),
				EmiStack.of(recipe.getGrowthStages().get(0).getBlock())
		);
		output = Stream.concat(
			Stream.concat(
				Stream.of(recipe.getOutput()),
				recipe.getAdditionalOutputs().stream())
			.map(EmiStack::of),
			recipe.getGrowthStages().stream().map(s -> EmiStack.of(s.getBlock())).filter(s -> !s.isEmpty())
		).toList();
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(input.get(0), 0, 8);

		widgets.addSlot(EmiStack.of(SpectrumBlocks.CRYSTALLARIEUM), 20, 18).drawBack(false);

		widgets.addFillingArrow(40, 9, recipe.getSecondsPerGrowthStage() * 1000);

		List<EmiStack> states = recipe.getGrowthStages().stream().map(s -> EmiStack.of(s.getBlock())).toList();
		widgets.addSlot(states.get(0), 20, 0);
		int x = 66;
		for (EmiStack stack : states) {
			widgets.addSlot(stack, x, 8).recipeContext(this);
			x += 20;
		}

		// catalysts
		widgets.addText(Text.translatable("container.spectrum.rei.crystallarieum.catalyst"), 0, 42, 0x3f3f3f, false);
		widgets.addText(Text.translatable("container.spectrum.rei.crystallarieum.accelerator"), 0, 58, 0x3f3f3f, false);
		widgets.addText(Text.translatable("container.spectrum.rei.crystallarieum.ink_consumption"), 0, 68, 0x3f3f3f, false);
		widgets.addText(Text.translatable("container.spectrum.rei.crystallarieum.used_up"), 0, 78, 0x3f3f3f, false);

		List<CrystallarieumCatalyst> catalysts = recipe.getCatalysts();
		for (int i = 0; i < catalysts.size(); i++) {
			CrystallarieumCatalyst catalyst = catalysts.get(i);
			int xOff = 46 + 18 * i;
			widgets.addSlot(EmiIngredient.of(catalyst.ingredient), xOff, 38);

			float growthAcceleration = catalyst.growthAccelerationMod;
			int uOff = growthAcceleration == 1 ? 97 : growthAcceleration >= 6 ? 85 : growthAcceleration > 1 ? 67 : growthAcceleration <= 0.25 ? 79 : 73;
			widgets.addTexture(BACKGROUND_TEXTURE, xOff + 5, 59, 6, 6, uOff, 0, 6, 6, 128, 128);

			float inkConsumption = catalyst.inkConsumptionMod;
			uOff = inkConsumption == 1 ? 97 : inkConsumption >= 8 ? 85 : inkConsumption > 1 ? 67 : inkConsumption <= 0.25 ? 79 : 73;
			widgets.addTexture(BACKGROUND_TEXTURE, xOff + 5, 69, 6, 6, uOff, 6, 6, 6, 128, 128);

			float consumeChance = catalyst.consumeChancePerSecond;
			uOff = consumeChance == 0 ? 97 : consumeChance >= 0.2 ? 85 : consumeChance >= 0.05 ? 67 : 91;
			widgets.addTexture(BACKGROUND_TEXTURE, xOff + 5, 79, 6, 6, uOff, 6, 6, 6, 128, 128);
		}

		if (recipe.growsWithoutCatalyst()) {
			widgets.addText(Text.translatable("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds", recipe.getSecondsPerGrowthStage()), 0, 90, 0x3f3f3f, false);
		} else {
			widgets.addText(Text.translatable("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_required", recipe.getSecondsPerGrowthStage()), 0, 90, 0x3f3f3f, false);
		}
	}
	
	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
