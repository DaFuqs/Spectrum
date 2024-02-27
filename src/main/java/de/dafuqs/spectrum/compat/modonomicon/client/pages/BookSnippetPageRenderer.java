package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.RenderedBookTextHolder;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.gui.book.markdown.MarkdownComponentRenderUtils;
import com.klikli_dev.modonomicon.client.render.page.BookPageRenderer;
import com.klikli_dev.modonomicon.client.render.page.PageWithTextRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookSnippetPage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;

public class BookSnippetPageRenderer extends BookPageRenderer<BookSnippetPage> implements PageWithTextRenderer {

    public BookSnippetPageRenderer(BookSnippetPage page) {
        super(page);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float ticks) {
        if (page.hasTitle()) {
            renderTitle(drawContext, page.getTitle(), page.showTitleSeparator(), BookContentScreen.PAGE_WIDTH / 2, 0);
        }

        if (page.getText() instanceof RenderedBookTextHolder renderedText) {
            int y = getTextY();
            for (MutableText component : renderedText.getRenderedText()) {
                var wrapped = MarkdownComponentRenderUtils.wrapComponents(component, BookContentScreen.PAGE_WIDTH, BookContentScreen.PAGE_WIDTH - 10, font);
                for (OrderedText orderedText : wrapped) {
                    drawCenteredStringNoShadow(drawContext, orderedText,
                            BookContentScreen.PAGE_WIDTH / 2, y, 0, 1);
                    y += font.fontHeight;
                }
            }
        } else {
            drawCenteredStringNoShadow(drawContext, page.getText().getComponent().asOrderedText(),
                    BookContentScreen.PAGE_WIDTH / 2, getTextY(), 0, 1);
        }

        RenderSystem.enableBlend();
        drawContext.drawTexture(page.getResourcePath(), 58 - page.getTextureWidth() / 2, getImageY(),
                page.getTextureX(), page.getTextureY(),
                page.getTextureWidth(), page.getTextureHeight(),
                page.getResourceWidth(), page.getResourceHeight());
    }

    private int getImageY() {
        if (page.hasTitle()) {
            return page.showTitleSeparator() ? 21 : 11;
        }

        return 0;
    }

    @Override
    public int getTextY() {
        return getImageY() + 4 + page.getTextureHeight();
    }

}
