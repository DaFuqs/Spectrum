package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.recipe.fluid_converting.FluidConvertingRecipe;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public abstract class PageFluidConverting<P extends FluidConvertingRecipe> extends PageDoubleRecipeRegistry<P> {
	
	public PageFluidConverting(RecipeType recipeType) {
		super(recipeType);
	}
	
	public abstract Identifier getBackgroundTexture();
	
	@Override
	protected ItemStack getRecipeOutput(FluidConvertingRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
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
		parent.renderItemStack(ms, recipeX + 75, recipeY + 7, mouseX, mouseY, recipe.getOutput());
	}
	
	@Override
	protected int getRecipeHeight() {
		return 50;
	}
	
}