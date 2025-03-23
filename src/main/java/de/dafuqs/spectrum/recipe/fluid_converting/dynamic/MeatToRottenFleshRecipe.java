package de.dafuqs.spectrum.recipe.fluid_converting.dynamic;

import com.neep.neepmeat.init.NMItems;
import de.dafuqs.spectrum.compat.SpectrumIntegrationPacks;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

public class MeatToRottenFleshRecipe extends DragonrotConvertingRecipe {
	
	public static final RecipeSerializer<MeatToRottenFleshRecipe> SERIALIZER = new EmptyRecipeSerializer<>(MeatToRottenFleshRecipe::new);
	
	public MeatToRottenFleshRecipe(Identifier identifier) {
		super(identifier, "", false, UNLOCK_IDENTIFIER, getMeatsIngredient(), Items.ROTTEN_FLESH.getDefaultStack());
	}
	
	private static Ingredient getMeatsIngredient() {
		return Ingredient.ofStacks(Registries.ITEM.stream().filter(item -> {
			FoodComponent foodComponent = item.getFoodComponent();
			return item != Items.ROTTEN_FLESH && !(SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.NEEPMEAT_ID) && item == NMItems.MEAT_SCRAP) && foodComponent != null && foodComponent.isMeat();
		}).map(ItemStack::new));
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
