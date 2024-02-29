package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconHelper;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookGatedRecipePage;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.matchbooks.recipe.IngredientStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class BookFusionShrineCraftingPageRenderer extends BookGatedRecipePageRenderer<FusionShrineRecipe, BookGatedRecipePage<FusionShrineRecipe>> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/fusion_shrine.png");

    public BookFusionShrineCraftingPageRenderer(BookGatedRecipePage<FusionShrineRecipe> page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 68;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, FusionShrineRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);

        renderTitle(drawContext, recipeY, second);

        // the ingredients
        List<IngredientStack> ingredients = recipe.getIngredientStacks();
        int startX = Math.max(-5, 30 - ingredients.size() * 8);
        for (int i = 0; i < ingredients.size(); i++) {
            ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + startX + i * 16, recipeY + 3, mouseX, mouseY, ingredients.get(i));
        }

        if (recipe.getFluidInput() != Fluids.EMPTY) {
            Item fluidBucketItem = recipe.getFluidInput().getBucketItem();
            if (fluidBucketItem != null) {
                // the shrine
                parentScreen.renderItemStack(drawContext, recipeX + 14, recipeY + 31, mouseX, mouseY, recipe.createIcon());

                // the fluid as a bucket
                ItemStack fluidBucketItemStack = new ItemStack(fluidBucketItem);
                parentScreen.renderItemStack(drawContext, recipeX + 30, recipeY + 31, mouseX, mouseY, fluidBucketItemStack);

            }
        } else {
            // the shrine
            parentScreen.renderItemStack(drawContext, recipeX + 22, recipeY + 31, mouseX, mouseY, recipe.createIcon());
        }

        // the output
        parentScreen.renderItemStack(drawContext, recipeX + 78, recipeY + 31, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
    }

}
