package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.energy.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.neoforge.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;
import java.util.stream.*;

public class CrystallarieumEmiRecipeGated extends GatedSpectrumEmiRecipe<CrystallarieumRecipe> {
	
	private final static ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/crystallarieum.png");
	private static final int LINE_HEIGHT = 10;
	
	public CrystallarieumEmiRecipeGated(RecipeHolder<CrystallarieumRecipe> entry) {
		super(SpectrumEmiRecipeCategories.CRYSTALLARIEUM, entry, 124, 100);
		inputs = List.of(
				EmiIngredient.of(recipe.getIngredientStack()),
				EmiStack.of(recipe.getGrowthStages().getFirst().getBlock())
		);
		outputs = Stream.concat(
				Stream.concat(
								Stream.of(recipe.getResultItem(getRegistryManager())),
								recipe.getAdditionalResults().stream())
						.map(EmiStack::of),
				recipe.getGrowthStages().stream().map(s -> EmiStack.of(s.getBlock())).filter(s -> !s.isEmpty())
		).toList();
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(inputs.getFirst(), 0, 0);
		widgets.addSlot(NeoForgeEmiIngredient.of(recipe.getFluidIngredient()), 0, 18);
		
		widgets.addSlot(EmiStack.of(CrystallarieumBlock.withColor(SpectrumBlocks.CRYSTALLARIEUM.toStack(), recipe.getInkColor())), 20, 18).drawBack(false);
		
		widgets.addFillingArrow(40, 9, recipe.getSecondsPerGrowthStage() * 1000);
		
		List<EmiStack> states = recipe.getGrowthStages().stream().map(s -> EmiStack.of(s.getBlock())).toList();
		Iterator<EmiStack> it = states.iterator();
		widgets.addSlot(it.next(), 20, 0);
		int x = 66;
		while (it.hasNext()) {
			widgets.addSlot(it.next(), x, 8).recipeContext(this);
			x += 20;
		}
		
		// catalysts
		widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.catalyst"), 0, 43, 0x3f3f3f, false);
		widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.speed"), 0, 49 + LINE_HEIGHT, 0x3f3f3f, false);
		widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.ink_drain"), 0, 49 + LINE_HEIGHT * 2, 0x3f3f3f, false);
		widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.depletion"), 0, 49 + LINE_HEIGHT * 3, 0x3f3f3f, false);
		
		List<CrystallarieumCatalyst> catalysts = recipe.getCatalysts();
		for (int i = 0; i < catalysts.size(); i++) {
			CrystallarieumCatalyst catalyst = catalysts.get(i);
			int xOff = 46 + 18 * i;
			widgets.addSlot(EmiIngredient.of(catalyst.ingredient()), xOff, 38);
			int offsetU = CrystallarieumRecipe.growthSpeedOffsetU(catalyst);
			widgets.addTexture(BACKGROUND_TEXTURE, xOff + 5, 59, 7, 7, offsetU, CrystallarieumRecipe.GROWTH_SPEED_V, 7, 7, 128, 128);
			
			offsetU = CrystallarieumRecipe.consumptionOffsetU(catalyst, offsetU);
			widgets.addTexture(BACKGROUND_TEXTURE, xOff + 5, 69, 7, 7, offsetU, CrystallarieumRecipe.CONSUMPTION_V, 7, 7, 128, 128);
			
			offsetU = CrystallarieumRecipe.consumeChanceOffsetU(catalyst, offsetU);
			widgets.addTexture(BACKGROUND_TEXTURE, xOff + 5, 79, 7, 7, offsetU, CrystallarieumRecipe.CONSUME_CHANCE_V, 7, 7, 128, 128);
		}
		
		if (recipe.growsWithoutCatalyst()) {
			widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_optional", recipe.getSecondsPerGrowthStage()), 0, 90, 0x3f3f3f, false);
		} else {
			widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds", recipe.getSecondsPerGrowthStage()), 0, 90, 0x3f3f3f, false);
		}
	}
	
	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
