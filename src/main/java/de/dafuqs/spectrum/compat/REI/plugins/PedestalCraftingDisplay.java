package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.recipe.pedestal.color.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.client.*;
import net.minecraft.util.collection.*;

import java.util.*;

public class PedestalCraftingDisplay extends GatedSpectrumDisplay {
	
	protected final PedestalRecipeTier pedestalRecipeTier;
	protected final int width;
	protected final int height;
	protected final float experience;
	protected final int craftingTime;
	public boolean shapeless;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PedestalCraftingDisplay(PedestalRecipe recipe) {
		super(recipe, mapIngredients(recipe), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
		this.pedestalRecipeTier = recipe.getTier();
		this.width = recipe.getWidth();
		this.height = recipe.getHeight();
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		this.shapeless = recipe.isShapeless();
	}
	
	private static List<EntryIngredient> mapIngredients(PedestalRecipe recipe) {
		int powderSlotCount = recipe.getTier().getPowderSlotCount();
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		int ingredientCount = ingredients.size();
		
		List<EntryIngredient> list = DefaultedList.ofSize(9 + powderSlotCount, EntryIngredient.empty());
		
		for (int i = 0; i < ingredientCount; i++) {
			list.set(recipe.getGridSlotId(i), REIHelper.ofIngredientStack(recipe.getIngredientStacks().get(i)));
		}
		for (int i = 0; i < powderSlotCount; i++) {
			BuiltinGemstoneColor color = BuiltinGemstoneColor.values()[i];
			int powderAmount = recipe.getPowderInputs().getOrDefault(color, 0);
			if (powderAmount > 0) {
				list.set(9 + i, EntryIngredients.of(color.getGemstonePowderItem(), powderAmount));
			}
		}
		return list;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.PEDESTAL_CRAFTING;
	}
	
	@Override
	@SuppressWarnings("resource")
	public boolean isUnlocked() {
		return this.pedestalRecipeTier.hasUnlocked(MinecraftClient.getInstance().player) && super.isUnlocked();
	}
	
	public PedestalRecipeTier getTier() {
		return this.pedestalRecipeTier;
	}
	
}