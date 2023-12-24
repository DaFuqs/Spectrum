package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.client.render.page.BookSpotlightPageRenderer;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookNbtSpotlightPage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;

public class BookNbtSpotlightPageRenderer extends BookSpotlightPageRenderer {

    public BookNbtSpotlightPageRenderer(BookNbtSpotlightPage page) {
        super(page);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float ticks) {
        if (page instanceof BookNbtSpotlightPage nbtPage && nbtPage.isPageUnlocked()) {
            super.render(drawContext, mouseX, mouseY, ticks);
        }
    }

    @Nullable
    @Override
    public Style getClickedComponentStyleAt(double pMouseX, double pMouseY) {
        if (page instanceof BookNbtSpotlightPage nbtPage && nbtPage.isPageUnlocked()) {
            return super.getClickedComponentStyleAt(pMouseX, pMouseY);
        }
        return null;
    }

}
