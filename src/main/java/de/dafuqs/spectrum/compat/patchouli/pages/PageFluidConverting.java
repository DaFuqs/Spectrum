package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.compat.patchouli.PatchouliHelper;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

public abstract class PageFluidConverting<P extends FluidConvertingRecipe> extends PageGatedRecipe<P> {
	
	public PageFluidConverting(RecipeType<P> recipeType) {
		super(recipeType);
	}
	
	public abstract Identifier getBackgroundTexture();
	
	@Override
	protected ItemStack getRecipeOutput(World world, FluidConvertingRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
		}
	}
	
	@Override
	protected void drawRecipe(DrawContext drawContext, @NotNull FluidConvertingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, getBackgroundTexture());
		RenderSystem.enableBlend();
		PatchouliHelper.drawBookBackground(getBackgroundTexture(), drawContext, recipeX, recipeY);

		parent.drawCenteredStringNoShadow(drawContext, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// fluid bucket
		parent.renderItemStack(drawContext, recipeX - 1, recipeY + 15, mouseX, mouseY, recipe.createIcon());
		
		// the ingredients
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		parent.renderIngredient(drawContext, recipeX + 23, recipeY + 7, mouseX, mouseY, ingredients.get(0));
		
		// the output
		parent.renderItemStack(drawContext, recipeX + 75, recipeY + 7, mouseX, mouseY, recipe.getOutput(DynamicRegistryManager.EMPTY));
	}
	
	@Override
	protected int getRecipeHeight() {
		return 50;
	}
	
}