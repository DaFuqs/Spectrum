package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import oshi.util.tuples.*;

import java.util.*;

public class StarCandyRecipe extends PedestalCraftingRecipe {
	
	public static final RecipeSerializer<StarCandyRecipe> SERIALIZER = new EmptyRecipeSerializer<>(StarCandyRecipe::new);
	public static final Random RANDOM = new Random();
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/food/star_candy");
	public static final float PURPLE_STAR_CANDY_CHANCE = 0.02F;
	public static final Map<BuiltinGemstoneColor, Integer> GEMSTONE_POWDER_INPUTS = Map.of(BuiltinGemstoneColor.YELLOW, 1);
	
	public StarCandyRecipe(Identifier id) {
		super(id, "", false, UNLOCK_IDENTIFIER, PedestalRecipeTier.SIMPLE, 3, 3, generateInputs(), GEMSTONE_POWDER_INPUTS, SpectrumItems.STAR_CANDY.getDefaultStack(), 1.0F, 20, false, false);
	}

	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		if (inv instanceof PedestalBlockEntity pedestal) {
			Triplet<Integer, Integer, Boolean> orientation = getRecipeOrientation(inv);
			if (orientation == null) {
				return ItemStack.EMPTY;
			}
			decrementIngredientStacks(pedestal, orientation);
			
			ItemStack recipeOutput;
			if (RANDOM.nextFloat() < PURPLE_STAR_CANDY_CHANCE) {
				recipeOutput = SpectrumItems.PURPLE_STAR_CANDY.getDefaultStack();
			} else {
				recipeOutput = this.output.copy();
			}
			
			PlayerEntity player = pedestal.getOwnerIfOnline();
			if (player != null) {
				recipeOutput.onCraft(pedestal.getWorld(), player, recipeOutput.getCount());
			}
			return recipeOutput;
		}
		return ItemStack.EMPTY;
	}
	
	private static DefaultedList<IngredientStack> generateInputs() {
		DefaultedList<IngredientStack> inputs = DefaultedList.ofSize(9);
		inputs.add(IngredientStack.of(Ingredient.ofItems(Items.SUGAR)));
		inputs.add(IngredientStack.of(Ingredient.ofItems(Items.SUGAR)));
		inputs.add(IngredientStack.of(Ingredient.ofItems(Items.SUGAR)));
		inputs.add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.STARDUST)));
		inputs.add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.STARDUST)));
		inputs.add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.STARDUST)));
		inputs.add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS)));
		inputs.add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS)));
		inputs.add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS)));
		return inputs;
	}
	
	
}
