package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.recipe.pedestal.color.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.util.*;

import java.util.*;

public class PedestalCraftingEmiRecipeGated extends GatedSpectrumEmiRecipe<PedestalRecipe> {
	
	public PedestalCraftingEmiRecipeGated(PedestalRecipe recipe) {
		super(SpectrumEmiRecipeCategories.PEDESTAL_CRAFTING, null, recipe, 124, 90);
		this.input = getIngredients(recipe);
	}
	
	@Override
	public boolean isUnlocked() {
		MinecraftClient client = MinecraftClient.getInstance();
		return recipe.getTier().hasUnlocked(client.player) && super.isUnlocked();
	}
	
	private static List<EmiIngredient> getIngredients(PedestalRecipe recipe) {
		int powderSlotCount = recipe.getTier().getPowderSlotCount();
		
		List<EmiIngredient> list = new ArrayList<>(9 + powderSlotCount);
		for (int i = 0; i < 9 + powderSlotCount; i++) {
			list.add(EmiStack.EMPTY);
		}
		for (int i = 0; i < recipe.getIngredientStacks().size(); i++) {
			list.set(getSlotWithSize(recipe.getWidth(), i), EmiIngredient.of(recipe.getIngredientStacks().get(i).getStacks().stream().map(EmiStack::of).toList()));
		}
		
		for (int i = 0; i < powderSlotCount; i++) {
			int amount = recipe.getPowderInputs().getOrDefault(BuiltinGemstoneColor.values()[i], 0);
			if (amount > 0) {
				list.set(PedestalBlockEntity.FIRST_POWDER_SLOT_ID + i, EmiStack.of(PedestalBlockEntity.getGemstonePowderItemForSlot(PedestalBlockEntity.FIRST_POWDER_SLOT_ID + i), amount));
			}
		}
		return list;
	}
	
	public static int getSlotWithSize(int recipeWidth, int index) {
		int x = index % recipeWidth;
		int y = (index - x) / recipeWidth;
		return 3 * y + x;
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		int powderSlotCount = recipe.getTier().getPowderSlotCount();
		int gemstoneSlotStartX = width / 2 + (powderSlotCount == 5 ? -45 : powderSlotCount == 4 ? -40 : -31);
		int gemstoneSlotTextureStartX = powderSlotCount == 5 ? 43 : powderSlotCount == 4 ? 52 : 61;
		
		Identifier backgroundTexture = PedestalScreen.getBackgroundTextureForTier(recipe.getTier());
		// gemstone slot background
		widgets.addTexture(backgroundTexture, gemstoneSlotStartX, 59, 18 * powderSlotCount, 18, gemstoneSlotTextureStartX, 76);
		// crafting input
		widgets.addTexture(backgroundTexture, 0, 0, 54, 54, 29, 18);
		// crafting output
		widgets.addTexture(backgroundTexture, 90, 14, 26, 26, 122, 32);
		// miniature gemstones
		widgets.addTexture(backgroundTexture, 82, 38, 40, 16, 200, 0);

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				widgets.addSlot(input.get(y * 3 + x), x * 18, y * 18).drawBack(false);
			}
		}

		// gemstone dust slots
		for (int i = 0; i < powderSlotCount; i++) {
			widgets.addSlot(input.get(PedestalBlockEntity.FIRST_POWDER_SLOT_ID + i), i * 18 + gemstoneSlotStartX, 59).drawBack(false);
		}

		widgets.addSlot(output.get(0), 90, 14).large(true).drawBack(false).recipeContext(this);
		widgets.addFillingArrow(60, 18, recipe.getCraftingTime() * 50);
		widgets.addText(getCraftingTimeText(recipe.getCraftingTime(), recipe.getExperience()), width / 2, 80, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}
}
