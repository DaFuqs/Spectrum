package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookPageRenderer;
import com.klikli_dev.modonomicon.client.render.page.PageWithTextRenderer;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookLinkPage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;

public class BookLinkPageRenderer extends BookPageRenderer<BookLinkPage> implements PageWithTextRenderer {

    public BookLinkPageRenderer(BookLinkPage page) {
        super(page);
    }

    @Override
    public void onBeginDisplayPage(BookContentScreen parentScreen, int left, int top) {
        super.onBeginDisplayPage(parentScreen, left, top);

        addButton(ButtonWidget.builder(page.getLinkText().getComponent(), (b) -> {})
                .position(BookContentScreen.PAGE_WIDTH / 2 - 50, BookContentScreen.PAGE_HEIGHT - 35)
                .size(100, 20)
                .build());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float ticks) {
        if (page.hasTitle()) {
            renderTitle(drawContext, page.getTitle(), page.showTitleSeparator(), BookContentScreen.PAGE_WIDTH / 2, 0);
        }

        renderBookTextHolder(drawContext, this.getPage().getText(), 0, this.getTextY(), BookContentScreen.PAGE_WIDTH);

        var style = getClickedComponentStyleAt(mouseX, mouseY);
        if (style != null)
            this.parentScreen.renderComponentHoverEffect(drawContext, page.getLinkText().getComponent().getStyle(), mouseX, mouseY);
    }

    @Nullable
    @Override
    public Style getClickedComponentStyleAt(double pMouseX, double pMouseY) {
        int buttonX = BookContentScreen.PAGE_WIDTH / 2 - 50;
        int buttonY = BookContentScreen.PAGE_HEIGHT - 35;

        if (pMouseX >= buttonX && pMouseY >= buttonY && pMouseX <= buttonX + 100 && pMouseY <= buttonY + 20) {
            return page.getLinkText().getComponent().getStyle();
        }

        return super.getClickedComponentStyleAt(pMouseX, pMouseY);
    }

    @Override
    public int getTextY() {
        if (page.hasTitle()) {
            return page.showTitleSeparator() ? 17 : 7;
        }
        return -4;
    }
}
