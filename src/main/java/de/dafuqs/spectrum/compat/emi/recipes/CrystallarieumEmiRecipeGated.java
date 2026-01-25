package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;
import java.util.stream.*;

public class CrystallarieumEmiRecipeGated extends GatedSpectrumEmiRecipe<CrystallarieumRecipe> {
	private final static ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/crystallarieum.png");
	
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
		widgets.addSlot(EmiStack.of(recipe.getFluidIngredient().getFluid()), 0, 18);
		
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
		widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.catalyst"), 0, 42, 0x3f3f3f, false);
		widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.accelerator"), 0, 58, 0x3f3f3f, false);
		widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.ink_consumption"), 0, 68, 0x3f3f3f, false);
		widgets.addText(Component.translatable("container.spectrum.rei.crystallarieum.used_up"), 0, 78, 0x3f3f3f, false);
		
		List<CrystallarieumCatalyst> catalysts = recipe.getCatalysts();
		for (int i = 0; i < catalysts.size(); i++) {
			CrystallarieumCatalyst catalyst = catalysts.get(i);
			int xOff = 46 + 18 * i;
			widgets.addSlot(EmiIngredient.of(catalyst.ingredient()), xOff, 38);
			int offset = 0;
			float accel = catalyst.growthAccelerationMod();
			
			if (accel > 0.2) {
				if (accel >= 5)
					offset = 7 * 4;
				else if (accel > 1)
					offset = 7 * 3;
				else if(accel == 1)
					offset = 7 * 2;
				else if (accel < 1)
					offset = 7;
			}
			widgets.addTexture(BACKGROUND_TEXTURE, xOff + 5, 59, 7, 7, 70 + offset, 0, 7, 7, 128, 128);
			
			float drain = catalyst.inkConsumptionMod();
			
			if (drain >= 5)
				offset = 0;
			else if (drain > 1)
				offset = 7;
			else if(drain == 1)
				offset = 7 * 2;
			else if (drain < 0.2)
				offset = 7 * 4;
			else if (drain < 1)
				offset = 7 * 3;
			
			widgets.addTexture(BACKGROUND_TEXTURE, xOff + 5, 69, 7, 7, 70 + offset, 7, 7, 7, 128, 128);
			
			float chance = catalyst.consumeChancePerSecond();
			
			if (chance >= 0.25)
				offset = 0;
			else if (chance < 0.0001)
				offset = 7 * 4;
			else if (chance <= 0.02)
				offset = 7 * 3;
			else if(chance < 0.05)
				offset = 7 * 2;
			else if (chance < 0.25)
				offset = 7;
			
			widgets.addTexture(BACKGROUND_TEXTURE, xOff + 5, 79, 7, 7, 70 + offset, 14, 7, 7, 128, 128);
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
