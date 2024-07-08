package de.dafuqs.spectrum.recipe.pedestal;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class ShapelessPedestalRecipe extends PedestalRecipe {
	
	public ShapelessPedestalRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
								   PedestalRecipeTier tier, List<IngredientStack> craftingInputs, Map<GemstoneColor, Integer> gemstonePowderInputs, ItemStack output,
								   float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades) {
		
		super(id, group, secret, requiredAdvancementIdentifier, tier, craftingInputs, gemstonePowderInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return matchIngredientStacksExclusively(inv, getIngredientStacks(), CRAFTING_GRID_SLOTS) && super.matches(inv, world);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.SHAPELESS_PEDESTAL_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.PEDESTAL;
	}
	
	@Override
	public void consumeIngredients(PedestalBlockEntity pedestal) {
		super.consumeIngredients(pedestal);
		
		for (int slot : CRAFTING_GRID_SLOTS) {
			for (IngredientStack ingredientStack : this.inputs) {
				ItemStack slotStack = pedestal.getStack(slot);
				if (ingredientStack.test(slotStack)) {
					decrementGridSlot(pedestal, slot, ingredientStack.getCount(), slotStack);
					break;
				}
			}
		}
	}
	
}
