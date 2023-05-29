package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.*;
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
import vazkii.patchouli.client.book.page.abstr.*;

public abstract class PageFluidConverting<P extends FluidConvertingRecipe> extends PageDoubleRecipeRegistry<P> {
	
	public PageFluidConverting(RecipeType recipeType) {
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
	protected void drawRecipe(MatrixStack ms, @NotNull FluidConvertingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		RenderSystem.setShaderTexture(0, getBackgroundTexture());
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);
		
		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// fluid bucket
		parent.renderItemStack(ms, recipeX - 1, recipeY + 15, mouseX, mouseY, recipe.createIcon());
		
		// the ingredients
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		parent.renderIngredient(ms, recipeX + 23, recipeY + 7, mouseX, mouseY, ingredients.get(0));
		
		// the output
		parent.renderItemStack(ms, recipeX + 75, recipeY + 7, mouseX, mouseY, recipe.getOutput(DynamicRegistryManager.EMPTY));
	}
	
	@Override
	protected int getRecipeHeight() {
		return 50;
	}
	
}