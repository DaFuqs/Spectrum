package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.world.item.crafting.*;

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
	public PedestalCraftingDisplay(RecipeHolder<PedestalRecipe> recipe) {
		super(recipe, mapIngredients(recipe.value()), Collections.singletonList(EntryIngredients.of(recipe.value().getResultItem(BasicDisplay.registryAccess()))));
		this.pedestalRecipeTier = recipe.value().getTier();
		this.width = recipe.value().getWidth();
		this.height = recipe.value().getHeight();
		this.experience = recipe.value().getExperience();
		this.craftingTime = recipe.value().getCraftingTime();
		this.shapeless = recipe.value().isShapeless();
	}
	
	private static List<EntryIngredient> mapIngredients(PedestalRecipe recipe) {
		int powderSlotCount = recipe.getTier().getPowderSlotCount();
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		int ingredientCount = ingredients.size();
		
		List<EntryIngredient> list = NonNullList.withSize(9 + powderSlotCount, EntryIngredient.empty());
		
		for (int i = 0; i < ingredientCount; i++) {
			list.set(recipe.getGridSlotId(i), REIHelper.ofIngredientStack(recipe.getIngredientStacks().get(i)));
		}
		for (int i = 0; i < powderSlotCount; i++) {
			GemstoneColor color = BuiltinGemstoneColor.values()[i];
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
    public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return this.pedestalRecipeTier.hasUnlocked(client.player) && super.isUnlocked();
	}
	
	public PedestalRecipeTier getTier() {
		return this.pedestalRecipeTier;
	}
	
}
