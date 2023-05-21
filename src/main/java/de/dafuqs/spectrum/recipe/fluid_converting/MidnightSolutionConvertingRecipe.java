package de.dafuqs.spectrum.recipe.fluid_converting;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MidnightSolutionConvertingRecipe extends FluidConvertingRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/create_midnight_aberration");
	private static final Set<Item> outputItems = new HashSet<>();
	
	public MidnightSolutionConvertingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, @NotNull Ingredient inputIngredient, ItemStack outputItemStack) {
		super(id, group, secret, requiredAdvancementIdentifier, inputIngredient, outputItemStack);
		outputItems.add(outputItemStack.getItem());
	}
	
	public static boolean isExistingOutputItem(@NotNull ItemStack itemStack) {
		return outputItems.contains(itemStack.getItem());
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING;
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING_ID;
	}
	
}
