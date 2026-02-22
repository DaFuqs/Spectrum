package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import org.jetbrains.annotations.*;

public class ShapedGatedCraftingRecipe extends ShapedRecipe implements GatedRecipe<RecipeInputInventory> {

	public final boolean secret;
	public final @Nullable Identifier requiredAdvancementIdentifier;

	public ShapedGatedCraftingRecipe(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output, boolean showNotification, boolean secret, @Nullable Identifier requiredAdvancementIdentifier) {
		super(id, group, category, width, height, input, output, showNotification);
		this.secret = secret;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
	}

	@Override
	public boolean isSecret() {
		return this.secret;
	}

	@Override
	public @Nullable Identifier getRequiredAdvancementIdentifier() {
		return this.requiredAdvancementIdentifier;
	}

	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return null;
	}

	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.SHAPED_GATED_CRAFTING_RECIPE_ID;
	}

}
