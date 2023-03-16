package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.util.*;

import java.util.*;

public class PedestalCraftingEmiRecipeGated extends GatedSpectrumEmiRecipe<PedestalCraftingRecipe> {
	private static final int GEMSTONE_SLOTS = 9;
	
	public PedestalCraftingEmiRecipeGated(PedestalCraftingRecipe recipe) {
		super(SpectrumEmiRecipeCategories.PEDESTAL_CRAFTING, recipe, 124, 90);
		input = getIngredients(recipe);
	}
	
	@Override
	public boolean isUnlocked() {
		MinecraftClient client = MinecraftClient.getInstance();
		return PedestalRecipeTier.hasUnlockedRequiredTier(client.player, recipe.getTier()) && super.isUnlocked();
	}

	private static List<EmiIngredient> getIngredients(PedestalCraftingRecipe recipe) {
		int shownGemstoneSlotCount = recipe.getTier() == PedestalRecipeTier.COMPLEX ? 5 : recipe.getTier() == PedestalRecipeTier.ADVANCED ? 4 : 3;
		
		List<EmiIngredient> list = new ArrayList<>(9 + shownGemstoneSlotCount);
		for (int i = 0; i < 9 + shownGemstoneSlotCount; i++) {
			list.add(EmiStack.EMPTY);
		}
		for (int i = 0; i < recipe.getIngredientStacks().size(); i++) {
			list.set(getSlotWithSize(recipe.getWidth(), i), EmiIngredient.of(recipe.getIngredientStacks().get(i).getStacks().stream().map(EmiStack::of).toList()));
		}

		for (int i = 0; i < shownGemstoneSlotCount; i++) {
			int amount = recipe.getGemstonePowderInputs().getOrDefault(BuiltinGemstoneColor.values()[i], 0);
			if (amount > 0) {
				list.set(GEMSTONE_SLOTS + i, EmiStack.of(PedestalBlockEntity.getGemstonePowderItemForSlot(GEMSTONE_SLOTS + i), amount));
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
		int shownGemstoneSlotCount = recipe.getTier() == PedestalRecipeTier.COMPLEX ? 5 : recipe.getTier() == PedestalRecipeTier.ADVANCED ? 4 : 3;
		int gemstoneSlotStartX = width / 2 + (shownGemstoneSlotCount == 5 ? -45 : shownGemstoneSlotCount == 4 ? -40 : -31);
		int gemstoneSlotTextureStartX = shownGemstoneSlotCount == 5 ? 43 : shownGemstoneSlotCount == 4 ? 52 : 61;

		Identifier backgroundTexture = PedestalScreen.getBackgroundTextureForTier(recipe.getTier());
		// the gemstone slot background texture
		widgets.addTexture(backgroundTexture, gemstoneSlotStartX, 59, 18 * shownGemstoneSlotCount, 18, gemstoneSlotTextureStartX, 76);
		// crafting input texture
		widgets.addTexture(backgroundTexture, 0, 0, 54, 54, 29, 18);
		// crafting output texture
		widgets.addTexture(backgroundTexture, 90, 14, 26, 26, 122, 32);
		// miniature gemstones texture
		widgets.addTexture(backgroundTexture, 82, 38, 40, 16, 200, 0);

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				widgets.addSlot(input.get(y * 3 + x), x * 18, y * 18).drawBack(false);
			}
		}

		// gemstone dust slots
		for (int x = 0; x < shownGemstoneSlotCount; x++) {
			widgets.addSlot(input.get(GEMSTONE_SLOTS + x), x * 18 + gemstoneSlotStartX, 59).drawBack(false);
		}

		widgets.addSlot(output.get(0), 90, 14).output(true).drawBack(false).recipeContext(this);

		widgets.addFillingArrow(60, 18, recipe.getCraftingTime() * 50);

		widgets.addText(getCraftingTimeText(recipe.getCraftingTime(), recipe.getExperience()), width / 2, 80, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}
}
