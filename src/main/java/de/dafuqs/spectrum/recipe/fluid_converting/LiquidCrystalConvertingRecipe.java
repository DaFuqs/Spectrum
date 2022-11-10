package de.dafuqs.spectrum.recipe.fluid_converting;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class LiquidCrystalConvertingRecipe extends FluidConvertingRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/enter_liquid_crystal");
	private static final Set<Item> outputItems = new HashSet<>();
	
	public LiquidCrystalConvertingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, @NotNull Ingredient inputIngredient, ItemStack outputItemStack) {
		super(id, group, secret, requiredAdvancementIdentifier, inputIngredient, outputItemStack);
		outputItems.add(outputItemStack.getItem());
	}
	
	public static boolean isExistingOutputItem(@NotNull ItemStack itemStack) {
		return outputItems.contains(itemStack.getItem());
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumItems.LIQUID_CRYSTAL_BUCKET);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING;
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING_ID;
	}
	
}
