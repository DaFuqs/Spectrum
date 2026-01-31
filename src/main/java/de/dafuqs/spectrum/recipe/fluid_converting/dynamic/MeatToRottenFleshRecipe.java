package de.dafuqs.spectrum.recipe.fluid_converting.dynamic;

import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MeatToRottenFleshRecipe extends DragonrotConvertingRecipe {
	
	public MeatToRottenFleshRecipe() {
		super("", false, Optional.of(UNLOCK_IDENTIFIER), getMeatsIngredient(), Items.ROTTEN_FLESH.getDefaultInstance());
	}
	
	private static Ingredient getMeatsIngredient() {
		// TODO: is there a matching tag we can use here, instead of filtering?
		return Ingredient.of(BuiltInRegistries.ITEM.getOrCreateTag(ItemTags.MEAT)
				.stream()
				.filter(item -> item.value() == Items.ROTTEN_FLESH)
				.map(ItemStack::new));
	}
	
	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.DRAGONROT_MEAT_TO_ROTTEN_FLESH;
	}
	
}
