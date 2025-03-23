package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookTextPageRenderer;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookLinkPage;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;

public class BookLinkPageRenderer extends BookTextPageRenderer {

    private static final int BUTTON_X = 2;
    private static final int BUTTON_Y = BookContentScreen.PAGE_HEIGHT - 3;
    private static final int BUTTON_WIDTH = BookContentScreen.PAGE_WIDTH - 12;
    private static final int BUTTON_HEIGHT = ButtonWidget.DEFAULT_HEIGHT;

    public BookLinkPageRenderer(BookLinkPage page) {
        super(page);
    }

    @Override
    public void onBeginDisplayPage(BookContentScreen parentScreen, int left, int top) {
        if (!(page instanceof BookLinkPage linkPage)) return;

        super.onBeginDisplayPage(parentScreen, left, top);

        addButton(ButtonWidget.builder(linkPage.getLinkText().getComponent(), (b) -> {})
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .position(BUTTON_X, BUTTON_Y)
                .build());
    }

    @Nullable
    @Override
    public Style getClickedComponentStyleAt(double pMouseX, double pMouseY) {
        if (!(page instanceof BookLinkPage linkPage)) return null;

        int buttonX = BUTTON_X;
        int buttonY = BUTTON_Y;

        if (pMouseX >= buttonX && pMouseY >= buttonY && pMouseX < buttonX + BUTTON_WIDTH && pMouseY < buttonY + BUTTON_HEIGHT) {
            return linkPage.getLinkText().getComponent().getStyle();
        }

        return super.getClickedComponentStyleAt(pMouseX, pMouseY);
    }

}
