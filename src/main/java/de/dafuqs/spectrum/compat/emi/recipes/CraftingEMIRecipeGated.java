package de.dafuqs.spectrum.compat.emi.recipes;

import com.google.common.collect.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.crafting.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import dev.emi.emi.recipe.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

// An amalgam of EmiShapedRecipe & EmiShapelessRecipe
public class CraftingEMIRecipeGated extends GatedSpectrumEmiRecipe<GatedCraftingRecipe> {

	protected boolean shapeless = false;
	
	public CraftingEMIRecipeGated(RecipeHolder<GatedCraftingRecipe> recipe) {
		super(VanillaEmiRecipeCategories.CRAFTING, recipe, 118, 54);
		if(recipe.value() instanceof ShapedGatedCraftingRecipe shapedGatedCraftingRecipe) {
			this.inputs = padIngredients(shapedGatedCraftingRecipe);
		} else {
			this.inputs = recipe.value().getIngredients().stream().map(EmiIngredient::of).toList();
			this.shapeless = true;
		}
	}
	
	private static List<EmiIngredient> padIngredients(ShapedGatedCraftingRecipe recipe) {
		List<EmiIngredient> list = Lists.newArrayList();
		int i = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (x >= recipe.getWidth() || y >= recipe.getHeight() || i >= recipe.getIngredients().size()) {
					list.add(EmiStack.EMPTY);
				} else {
					list.add(EmiIngredient.of(recipe.getIngredients().get(i++)));
				}
			}
		}
		return list;
	}

	public boolean canFit(int width, int height) {
		if(shapeless) {
			return this.inputs.size() <= width * height;
		} else {
			if (inputs.size() > 9) {
				return false;
			}
			for (int i = 0; i < inputs.size(); i++) {
				int x = i % 3;
				int y = i / 3;
				if (!inputs.get(i).isEmpty() && (x >= width || y >= height)) {
					return false;
				}
			}
			return true;
		}
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		if (shapeless) {
			widgets.addTexture(EmiTexture.SHAPELESS, 97, 0);
		}
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18);
		
		int sOff = 0;
		if (!shapeless) {
			if (canFit(1, 3)) {
				sOff -= 1;
			}
			if (canFit(3, 1)) {
				sOff -= 3;
			}
		}
		for (int i = 0; i < 9; i++) {
			int s = i + sOff;
			if (s >= 0 && s < this.inputs.size()) {
				widgets.addSlot(this.inputs.get(s), i % 3 * 18, i / 3 * 18);
			} else {
				widgets.addSlot(EmiStack.of(ItemStack.EMPTY), i % 3 * 18, i / 3 * 18);
			}
		}
		widgets.addSlot(this.outputs.getFirst(), 92, 14).large(true).recipeContext(this);
	}

}