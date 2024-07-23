package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class BookPotionWorkshopPageRenderer<T extends PotionWorkshopRecipe> extends BookGatedRecipePageRenderer<T, BookGatedRecipePage<T>> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/potion_workshop.png");

    public BookPotionWorkshopPageRenderer(BookGatedRecipePage<T> page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 97;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, T recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);

        renderTitle(drawContext, recipeY, second);

        // the ingredients
        List<IngredientStack> ingredients = recipe.getIngredientStacks();
        ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 20, recipeY + 62, mouseX, mouseY, ingredients.get(0));
        ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 58, recipeY + 5, mouseX, mouseY, ingredients.get(1));
        ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 20, recipeY + 9, mouseX, mouseY, ingredients.get(2));
        ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 3, recipeY + 32, mouseX, mouseY, ingredients.get(3));
        ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 37, recipeY + 32, mouseX, mouseY, ingredients.get(4));

        // the potion workshop
        parentScreen.renderItemStack(drawContext, recipeX + 82, recipeY + 42, mouseX, mouseY, recipe.createIcon());

        // the output
        parentScreen.renderItemStack(drawContext, recipeX + 82, recipeY + 24, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
    }

}
