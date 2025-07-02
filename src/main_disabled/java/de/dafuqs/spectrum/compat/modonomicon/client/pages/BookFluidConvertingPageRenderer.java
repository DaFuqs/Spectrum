package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public abstract class BookFluidConvertingPageRenderer<R extends GatedRecipe<?>, T extends BookGatedRecipePage<R>> extends BookGatedRecipePageRenderer<R, T> {

    public BookFluidConvertingPageRenderer(T page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 50;
    }

    @Override
	protected void drawRecipe(GuiGraphics drawContext, RecipeHolder<R> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        R recipe = recipeEntry.value();
		Level world = Minecraft.getInstance().level;
        if (world == null) return;

        RenderSystem.enableBlend();
		drawContext.blit(getBackgroundTexture(), recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);

        renderTitle(drawContext, recipeY, second);

        // fluid bucket
		parentScreen.renderItemStack(drawContext, recipeX - 1, recipeY + 15, mouseX, mouseY, recipe.getToastSymbol());

        // the ingredients
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
        parentScreen.renderIngredient(drawContext, recipeX + 23, recipeY + 7, mouseX, mouseY, ingredients.getFirst());

        // the output
		parentScreen.renderItemStack(drawContext, recipeX + 75, recipeY + 7, mouseX, mouseY, recipe.getResultItem(world.registryAccess()));
    }
	
	public abstract ResourceLocation getBackgroundTexture();

}
