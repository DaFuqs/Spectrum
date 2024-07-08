package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;

import java.util.*;

public class PedestalCraftingEmiRecipeGated extends GatedSpectrumEmiRecipe<PedestalRecipe> {
	
	public PedestalCraftingEmiRecipeGated(PedestalRecipe recipe) {
		super(SpectrumEmiRecipeCategories.PEDESTAL_CRAFTING, recipe, 124, 90);
		this.inputs = getIngredients(recipe);
	}
	
	@Override
	public boolean isUnlocked() {
		MinecraftClient client = MinecraftClient.getInstance();
		return recipe.getTier().hasUnlocked(client.player) && super.isUnlocked();
	}
	
	private static List<EmiIngredient> getIngredients(PedestalRecipe recipe) {
		int powderSlotCount = recipe.getTier().getPowderSlotCount();
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		int ingredientCount = ingredients.size();
		
		List<EmiIngredient> list = DefaultedList.ofSize(9 + powderSlotCount, EmiStack.EMPTY);
		
		for (int i = 0; i < ingredientCount; i++) {
			list.set(recipe.getGridSlotId(i), EmiIngredient.of(ingredients.get(i).getStacks().stream().map(EmiStack::of).toList()));
		}
		for (int i = 0; i < powderSlotCount; i++) {
			GemstoneColor color = BuiltinGemstoneColor.values()[i];
			int powderAmount = recipe.getPowderInputs().getOrDefault(color, 0);
			if (powderAmount > 0) {
				list.set(9 + i, EmiStack.of(color.getGemstonePowderItem(), powderAmount));
			}
		}
		return list;
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		int powderSlotCount = recipe.getTier().getPowderSlotCount();
		int gemstoneSlotStartX = 62 - powderSlotCount * 9;
		int gemstoneSlotTextureStartU = 88 - powderSlotCount * 9;
		
		Identifier backgroundTexture = PedestalScreen.getBackgroundTextureForTier(recipe.getTier());
		// gemstone slot background
		widgets.addTexture(backgroundTexture, gemstoneSlotStartX, 59, 18 * powderSlotCount, 18, gemstoneSlotTextureStartU, 76);
		// crafting input
		widgets.addTexture(backgroundTexture, 0, 0, 54, 54, 29, 18);
		// crafting output
		widgets.addTexture(backgroundTexture, 90, 14, 26, 26, 122, 32);
		// miniature gemstones
		widgets.addTexture(backgroundTexture, 82, 38, 40, 16, 200, 0);
		
		// crafting grid slots
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				widgets.addSlot(inputs.get(y * 3 + x), x * 18, y * 18).drawBack(false);
			}
		}
		
		// powder slots
		for (int i = 0; i < powderSlotCount; i++) {
			widgets.addSlot(inputs.get(9 + i), i * 18 + gemstoneSlotStartX, 59).drawBack(false);
		}
		
		if (recipe.isShapeless()) {
			widgets.addTexture(EmiTexture.SHAPELESS, 94, 0);
		}
		
		widgets.addSlot(outputs.get(0), 90, 14).large(true).drawBack(false).recipeContext(this);
		widgets.addFillingArrow(60, 18, recipe.getCraftingTime() * 50);
		widgets.addText(getCraftingTimeText(recipe.getCraftingTime(), recipe.getExperience()), width / 2, 80, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}
}
