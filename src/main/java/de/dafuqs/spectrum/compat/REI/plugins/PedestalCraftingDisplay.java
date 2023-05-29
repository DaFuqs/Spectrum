package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.registry.*;

import java.util.*;

public class PedestalCraftingDisplay extends GatedSpectrumDisplay {
	
	protected final PedestalRecipeTier pedestalRecipeTier;
	protected final int width;
	protected final int height;
	protected final float experience;
	protected final int craftingTime;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PedestalCraftingDisplay(PedestalCraftingRecipe recipe) {
		super(recipe, mapIngredients(recipe), Collections.singletonList(EntryIngredients.of(recipe.getOutput(DynamicRegistryManager.EMPTY))));
		this.pedestalRecipeTier = recipe.getTier();
		this.width = recipe.getWidth();
		this.height = recipe.getHeight();
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
	}
	
	private static List<EntryIngredient> mapIngredients(PedestalCraftingRecipe recipe) {
		int shownGemstoneSlotCount = recipe.getTier() == PedestalRecipeTier.COMPLEX ? 5 : recipe.getTier() == PedestalRecipeTier.ADVANCED ? 4 : 3;
		
		List<EntryIngredient> list = new ArrayList<>(9 + shownGemstoneSlotCount);
		for (int i = 0; i < 9 + shownGemstoneSlotCount; i++) {
			list.add(EntryIngredient.empty());
		}
		for (int i = 0; i < recipe.getIngredientStacks().size(); i++) {
			list.set(getSlotWithSize(recipe.getWidth(), i), REIHelper.ofIngredientStack(recipe.getIngredientStacks().get(i)));
		}
		
		HashMap<BuiltinGemstoneColor, Integer> gemstonePowderInputs = recipe.getGemstonePowderInputs();
		int firstGemstoneSlotId = 3 * 3;
		
		int cyan = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.CYAN, 0);
		if (cyan > 0) {
			list.set(firstGemstoneSlotId, EntryIngredients.of(SpectrumItems.TOPAZ_POWDER, cyan));
		}
		int magenta = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.MAGENTA, 0);
		if (magenta > 0) {
			list.set(firstGemstoneSlotId + 1, EntryIngredients.of(SpectrumItems.AMETHYST_POWDER, magenta));
		}
		int yellow = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.YELLOW, 0);
		if (yellow > 0) {
			list.set(firstGemstoneSlotId + 2, EntryIngredients.of(SpectrumItems.CITRINE_POWDER, yellow));
		}
		if (shownGemstoneSlotCount >= 4) {
			int black = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.BLACK, 0);
			if (black > 0) {
				list.set(firstGemstoneSlotId + 3, EntryIngredients.of(SpectrumItems.ONYX_POWDER, black));
			}
			if (shownGemstoneSlotCount == 5) {
				int white = gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.WHITE, 0);
				if (white > 0) {
					list.set(firstGemstoneSlotId + 4, EntryIngredients.of(SpectrumItems.MOONSTONE_POWDER, white));
				}
			}
		}
		
		return list;
	}
	
	public static int getSlotWithSize(int recipeWidth, int index) {
		int x = index % recipeWidth;
		int y = (index - x) / recipeWidth;
		return 3 * y + x;
	}
	
	/**
	 * When using Shift click on the plus button in the REI gui to autofill crafting grids & recipe favourites
	 */
	/*public PedestalCraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int width, int height, float experience, int craftingTime, Identifier requiredAdvancementIdentifier, String recipeTier, boolean secret) {
		super(requiredAdvancementIdentifier, secret, inputs, outputs);
		this.pedestalRecipeTier = PedestalRecipeTier.valueOf(recipeTier.toUpperCase(Locale.ROOT));
		this.width = width;
		this.height = height;
		this.experience = experience;
		this.craftingTime = craftingTime;
	}*/
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.PEDESTAL_CRAFTING;
	}
	
	@Override
	public boolean isUnlocked() {
		return PedestalRecipeTier.hasUnlockedRequiredTier(MinecraftClient.getInstance().player, this.pedestalRecipeTier) && super.isUnlocked();
	}
	
	public PedestalRecipeTier getTier() {
		return this.pedestalRecipeTier;
	}
	
}