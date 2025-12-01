package de.dafuqs.spectrum.recipe.fluid_converting.dynamic;

import com.neep.neepmeat.init.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.impl.recipe.ingredient.builtin.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public class MeatToRottenFleshRecipe extends DragonrotConvertingRecipe {
	
	public MeatToRottenFleshRecipe() {
		super("", false, Optional.of(UNLOCK_IDENTIFIER), getMeatsIngredient(), Items.ROTTEN_FLESH.getDefaultInstance());
	}
	
	private static Ingredient getMeatsIngredient() {
		if (SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.NEEPMEAT_ID)) {
			return new DifferenceIngredient(Ingredient.of(ItemTags.MEAT), Ingredient.of(Items.ROTTEN_FLESH, NMItems.MEAT_SCRAP)).toVanilla();
		} else {
			return new DifferenceIngredient(Ingredient.of(ItemTags.MEAT), Ingredient.of(Items.ROTTEN_FLESH)).toVanilla();
		}
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.DRAGONROT_MEAT_TO_ROTTEN_FLESH;
	}
	
}
