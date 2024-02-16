package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class StarCandyRecipe extends ShapedPedestalRecipe {
	
	public static final RecipeSerializer<StarCandyRecipe> SERIALIZER = new EmptyRecipeSerializer<>(StarCandyRecipe::new);
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/food/star_candy");
	public static final float PURPLE_STAR_CANDY_CHANCE = 0.02F;
	
	public StarCandyRecipe(Identifier id) {
		super(id, "", false, UNLOCK_IDENTIFIER, PedestalRecipeTier.BASIC, 3, 3, generateInputs(), Map.of(BuiltinGemstoneColor.YELLOW, 1), SpectrumItems.STAR_CANDY.getDefaultStack(), 1.0F, 20, false, false);
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		if (inv instanceof PedestalBlockEntity pedestal) {
			World world = pedestal.getWorld();
			if (world.random.nextFloat() < PURPLE_STAR_CANDY_CHANCE) {
				return SpectrumItems.PURPLE_STAR_CANDY.getDefaultStack();
			}
		}
		return this.output.copy();
	}
	
	private static List<IngredientStack> generateInputs() {
		return List.of(
				IngredientStack.of(Ingredient.ofItems(Items.SUGAR)),
				IngredientStack.of(Ingredient.ofItems(Items.SUGAR)),
				IngredientStack.of(Ingredient.ofItems(Items.SUGAR)),
				IngredientStack.of(Ingredient.ofItems(SpectrumItems.STARDUST)),
				IngredientStack.of(Ingredient.ofItems(SpectrumItems.STARDUST)),
				IngredientStack.of(Ingredient.ofItems(SpectrumItems.STARDUST)),
				IngredientStack.of(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS)),
				IngredientStack.of(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS)),
				IngredientStack.of(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS)));
	}
	
	
}
