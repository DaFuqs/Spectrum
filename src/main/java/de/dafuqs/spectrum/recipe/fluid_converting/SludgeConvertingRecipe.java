package de.dafuqs.spectrum.recipe.fluid_converting;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SludgeConvertingRecipe extends FluidConvertingRecipe {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/sludge");
	private static final Set<Item> outputItems = new HashSet<>();
	
	public SludgeConvertingRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, @NotNull Ingredient inputIngredient, ItemStack outputItemStack) {
		super(group, secret, requiredAdvancementIdentifier, inputIngredient, outputItemStack);
		outputItems.add(outputItemStack.getItem());
	}
	
	public static boolean isExistingOutputItem(@NotNull ItemStack itemStack) {
		return outputItems.contains(itemStack.getItem());
	}
	
	@Override
	public @NotNull ItemStack getToastSymbol() {
		return new ItemStack(SpectrumItems.SLUDGE_BUCKET.get());
	}
	
	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.SLUDGE_CONVERTING_SERIALIZER;
	}
	
	@Override
	public @NotNull RecipeType<?> getType() {
		return SpectrumRecipeTypes.SLUDGE_CONVERTING;
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "sludge_converting";
	}
	
}
