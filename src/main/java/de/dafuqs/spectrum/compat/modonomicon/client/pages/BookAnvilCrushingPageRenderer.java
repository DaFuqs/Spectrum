package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookAnvilCrushingPage;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BookAnvilCrushingPageRenderer extends BookGatedRecipePageRenderer<AnvilCrushingRecipe, BookAnvilCrushingPage> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/anvil_crushing.png");

    public BookAnvilCrushingPageRenderer(BookAnvilCrushingPage page) {
        super(page);
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, AnvilCrushingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        BookTextHolder title = second ? page.getTitle2() : page.getTitle1();
        int titleY = second ? (page.getTitle2().isEmpty() ? 10 : 0) - 10 : -5;

        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX, recipeY + 4, 0, 0, 84, 48, 256, 256);

        if (!title.isEmpty()) {
            super.renderTitle(drawContext, title, false, BookContentScreen.PAGE_WIDTH / 2, titleY);
        }

        parentScreen.renderIngredient(drawContext, recipeX + 16, recipeY + 35, mouseX, mouseY, recipe.getIngredients().get(0));
        parentScreen.renderItemStack(drawContext, recipeX + 16, recipeY + 15, mouseX, mouseY, recipe.createIcon());
        parentScreen.renderItemStack(drawContext, recipeX + 64, recipeY + 29, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
    }

    @Override
    protected int getRecipeHeight() {
        return 73;
    }

}
