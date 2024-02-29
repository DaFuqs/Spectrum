package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.api.recipe.GatedRecipe;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookGatedRecipePage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public abstract class BookFluidConvertingPageRenderer<R extends GatedRecipe, T extends BookGatedRecipePage<R>> extends BookGatedRecipePageRenderer<R, T> {

    public BookFluidConvertingPageRenderer(T page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 50;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, R recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(getBackgroundTexture(), recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);

        renderTitle(drawContext, recipeY, second);

        // fluid bucket
        parentScreen.renderItemStack(drawContext, recipeX - 1, recipeY + 15, mouseX, mouseY, recipe.createIcon());

        // the ingredients
        DefaultedList<Ingredient> ingredients = recipe.getIngredients();
        parentScreen.renderIngredient(drawContext, recipeX + 23, recipeY + 7, mouseX, mouseY, ingredients.get(0));

        // the output
        parentScreen.renderItemStack(drawContext, recipeX + 75, recipeY + 7, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
    }

    public abstract Identifier getBackgroundTexture();

}
