package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.*;
import com.klikli_dev.modonomicon.client.gui.book.markdown.*;
import com.klikli_dev.modonomicon.client.render.page.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import net.minecraft.client.gui.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

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
                var wrapped = MarkdownComponentRenderUtils.wrapComponents(component, BookContentScreen.PAGE_WIDTH - 10, BookContentScreen.PAGE_WIDTH - 10, font);
                for (OrderedText orderedText : wrapped) {
                    drawCenteredStringNoShadow(drawContext, orderedText, page.getBook().getBookTextOffsetWidth() + BookContentScreen.PAGE_WIDTH / 2, y, 0, 1);
                    y += font.fontHeight;
                }
            }
        } else {
            drawCenteredStringNoShadow(drawContext, page.getText().getComponent().asOrderedText(), BookContentScreen.PAGE_WIDTH / 2, getTextY(), 0, 1);
        }

        RenderSystem.enableBlend();
        drawContext.drawTexture(page.getResourcePath(), 58 - page.getTextureWidth() / 2, getImageY(),
                page.getTextureX(), page.getTextureY(),
                page.getTextureWidth(), page.getTextureHeight(),
                page.getResourceWidth(), page.getResourceHeight());

        var style = this.getClickedComponentStyleAt(mouseX, mouseY);
        if (style != null)
            this.parentScreen.renderComponentHoverEffect(drawContext, style, mouseX, mouseY);
    }

    @Nullable
    @Override
    public Style getClickedComponentStyleAt(double pMouseX, double pMouseY) {
        if (pMouseX > 0 && pMouseY > 0) {
            if (page.hasTitle()) {
                var titleStyle = getClickedComponentStyleAtForTitle(page.getTitle(), BookContentScreen.PAGE_WIDTH / 2, 0, pMouseX, pMouseY);
                if (titleStyle != null) {
                    return titleStyle;
                }
            }

            var textStyle = getClickedComponentStyleAtForTextHolder(page.getText(), BookContentScreen.PAGE_WIDTH / 2, getTextY(), BookContentScreen.PAGE_WIDTH, pMouseX, pMouseY);
            if (textStyle != null) {
                return textStyle;
            }
        }
        return super.getClickedComponentStyleAt(pMouseX, pMouseY);
    }

    @Nullable
    @Override
    protected Style getClickedComponentStyleAtForTextHolder(BookTextHolder text, int x, int y, int width, double pMouseX, double pMouseY) {
        if (text.hasComponent()) {
            for (OrderedText formattedCharSequence : font.wrapLines(text.getComponent(), width)) {
                if (pMouseY > y && pMouseY < y + font.fontHeight) {
                    x -= font.getWidth(formattedCharSequence) / 2;
                    if (pMouseX < x)
                        return null;
                    return font.getTextHandler().getStyleAt(formattedCharSequence, (int) pMouseX - x);
                }
                y += font.fontHeight;
            }
        } else if (text instanceof RenderedBookTextHolder renderedText) {
            var components = renderedText.getRenderedText();
            for (var component : components) {
                var wrapped = MarkdownComponentRenderUtils.wrapComponents(component, width, width - 10, font);
                for (OrderedText formattedCharSequence : wrapped) {
                    if (pMouseY > y && pMouseY < y + font.fontHeight) {
                        x -= font.getWidth(formattedCharSequence) / 2;
                        if (pMouseX < x)
                            return null;
                        return font.getTextHandler().getStyleAt(formattedCharSequence, (int) pMouseX - x);
                    }
                    y += font.fontHeight;
                }
            }
        }

        return null;
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
