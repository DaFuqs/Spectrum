package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookConditionalPage;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import net.minecraft.client.gui.DrawContext;

public abstract class BookGatedRecipePageRenderer<R extends GatedRecipe, T extends BookRecipePage<R> & BookConditionalPage> extends BookRecipePageRenderer<R, T> {

    public BookGatedRecipePageRenderer(T page) {
        super(page);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float ticks) {
        if (page.isPageUnlocked()) {
            super.render(drawContext, mouseX, mouseY, ticks);
        }
    }

}
