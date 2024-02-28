package de.dafuqs.spectrum.recipe.pedestal;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import oshi.util.tuples.*;

import java.util.*;

public class ShapedPedestalRecipe extends PedestalRecipe {
	
	protected final int width;
	protected final int height;
	
	public ShapedPedestalRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
								PedestalRecipeTier tier, int width, int height, List<IngredientStack> inputs, Map<GemstoneColor, Integer> gemstonePowderInputs, ItemStack output,
								float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades) {
		super(id, group, secret, requiredAdvancementIdentifier, tier, inputs, gemstonePowderInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
		
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return getRecipeOrientation(inv) != null && super.matches(inv, world);
	}
	
	// Triplet<XOffset, YOffset, Flipped>
	public Triplet<Integer, Integer, Boolean> getRecipeOrientation(Inventory inv) {
		for (int i = 0; i <= 3 - this.width; ++i) {
			for (int j = 0; j <= 3 - this.height; ++j) {
				if (this.matchesPattern(inv, i, j, true)) {
					return new Triplet<>(i, j, true);
				}
				if (this.matchesPattern(inv, i, j, false)) {
					return new Triplet<>(i, j, false);
				}
			}
		}
		return null;
	}
	
	public boolean matchesPattern(Inventory inv, int offsetX, int offsetY, boolean flipped) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int k = i - offsetX;
				int l = j - offsetY;
				IngredientStack ingredient = IngredientStack.EMPTY;
				if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
					if (flipped) {
						ingredient = this.inputs.get(this.width - k - 1 + l * this.width);
					} else {
						ingredient = this.inputs.get(k + l * this.width);
					}
				}
				
				if (!ingredient.test(inv.getStack(i + j * 3))) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void consumeIngredients(PedestalBlockEntity pedestal) {
		super.consumeIngredients(pedestal);
		
		Triplet<Integer, Integer, Boolean> orientation = getRecipeOrientation(pedestal);
		if (orientation == null) {
			return;
		}
		
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				int ingredientStackId = orientation.getC() ? ((this.width - 1) - x) + this.width * y : x + this.width * y;
				int slot = (x + orientation.getA()) + 3 * (y + orientation.getB());
				
				IngredientStack ingredientStackAtPos = this.inputs.get(ingredientStackId);
				ItemStack slotStack = pedestal.getStack(slot);
				if (!ingredientStackAtPos.test(slotStack)) {
					SpectrumCommon.logError("Looks like DaFuqs fucked up Spectrums Pedestal recipe matching. Go open up a report with the recipe that was crafted and an image of the pedestals contents, please! :)");
				}
				
				if (!slotStack.isEmpty()) {
					decrementGridSlot(pedestal, slot, ingredientStackAtPos.getCount(), slotStack);
				}
			}
		}
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.SHAPED_PEDESTAL_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.PEDESTAL;
	}
	
	@Override
	public int getWidth() {
		return this.width;
	}
	
	@Override
	public int getHeight() {
		return this.height;
	}
	
	@Override
	public boolean isShapeless() {
		return false;
	}
	
}
