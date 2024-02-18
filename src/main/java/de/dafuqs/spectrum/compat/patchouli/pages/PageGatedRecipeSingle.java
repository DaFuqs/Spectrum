package de.dafuqs.spectrum.compat.patchouli.pages;

import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.recipe.*;
import net.minecraft.world.*;

/**
 * Like PageGatedRecipeDouble, but only displays a single recipe
 */
public abstract class PageGatedRecipeSingle<T extends GatedRecipe> extends PageGatedRecipe<T> {
	
	public PageGatedRecipeSingle(RecipeType<T> recipeType) {
		super(recipeType);
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
		if (recipe != null) {
			World world = MinecraftClient.getInstance().world;
			if (world == null) {
				return;
			}
			
			int recipeX = getX();
			int recipeY = getY();
			drawRecipe(drawContext, world, recipe, recipeX, recipeY, mouseX, mouseY);
		}
		super.render(drawContext, mouseX, mouseY, tickDelta);
	}
	
	protected abstract void drawRecipe(DrawContext drawContext, World world, T recipe, int recipeX, int recipeY, int mouseX, int mouseY);
	
}
