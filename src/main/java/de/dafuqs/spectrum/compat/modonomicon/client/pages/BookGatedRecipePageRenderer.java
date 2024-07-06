package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import de.dafuqs.spectrum.api.recipe.GatedRecipe;
import net.minecraft.client.gui.DrawContext;

public abstract class BookGatedRecipePageRenderer<R extends GatedRecipe, T extends BookRecipePage<R>> extends BookRecipePageRenderer<R, T> {

    public BookGatedRecipePageRenderer(T page) {
        super(page);
    }

    public void renderTitle(DrawContext drawContext, int recipeY, boolean second) {
        BookTextHolder title = second ? page.getTitle2() : page.getTitle1();
        if (!title.getString().isEmpty()) {
            int titleY = second ? recipeY - (page.getTitle2().isEmpty() ? 10 : 0) - 10 : -5;
            super.renderTitle(drawContext, title, false, BookContentScreen.PAGE_WIDTH / 2, titleY);
        }
    }

}
