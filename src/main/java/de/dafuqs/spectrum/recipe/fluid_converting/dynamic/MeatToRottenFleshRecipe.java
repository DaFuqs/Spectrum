package de.dafuqs.spectrum.recipe.fluid_converting.dynamic;

import de.dafuqs.spectrum.recipe.fluid_converting.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public class MeatToRottenFleshRecipe extends DragonrotConvertingRecipe {
	
	public static final RecipeSerializer<MeatToRottenFleshRecipe> SERIALIZER = new SpecialRecipeSerializer<>(MeatToRottenFleshRecipe::new);
	
	public MeatToRottenFleshRecipe(Identifier identifier) {
		super(identifier, "", false, UNLOCK_IDENTIFIER, getMeatsIngredient(), Items.ROTTEN_FLESH.getDefaultStack());
	}
	
	private static Ingredient getMeatsIngredient() {
		return Ingredient.ofStacks(Registry.ITEM.stream().filter(item -> {
			FoodComponent foodComponent = item.getFoodComponent();
			return item != Items.ROTTEN_FLESH && foodComponent != null && foodComponent.isMeat();
		}).map(ItemStack::new));
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
