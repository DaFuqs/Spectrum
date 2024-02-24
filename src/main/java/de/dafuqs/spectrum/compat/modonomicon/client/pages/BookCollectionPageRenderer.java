package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookPageRenderer;
import com.klikli_dev.modonomicon.client.render.page.PageWithTextRenderer;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookCollectionPage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BookCollectionPageRenderer extends BookPageRenderer<BookCollectionPage> implements PageWithTextRenderer {

    private static final int ENTRIES_PER_ROW = 6;

    public BookCollectionPageRenderer(BookCollectionPage page) {
        super(page);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float pticks) {
        if (page.hasTitle()) {
            this.renderTitle(drawContext, page.getTitle(), page.showTitleSeparator(), BookContentScreen.PAGE_WIDTH / 2, 0);
        }

        this.renderBookTextHolder(drawContext, this.getPage().getText(), 0, this.getTextY(), BookContentScreen.PAGE_WIDTH);

        List<ItemStack> items = page.getItems();
        int startY = page.hasTitle() ? 18 : 0;
        int row = 0;
        int column = -1;
        int firstNonFullRowIndex = (items.size()) / ENTRIES_PER_ROW;
        int unusedEntriesInLastRow = ENTRIES_PER_ROW - (items.size() % ENTRIES_PER_ROW);
        for (ItemStack stack : items) {
            column++;
            if (column == ENTRIES_PER_ROW) {
                column = 0;
                row++;
            }
            int startX = 5 + column * 18;
            if (row == firstNonFullRowIndex) {
                startX += unusedEntriesInLastRow * 9;
            }
            parentScreen.renderItemStack(drawContext, startX, startY + row * 18, mouseX, mouseY, stack);
        }
    }

    @Override
    public int getTextY() {
        return 8 + (page.hasTitle() ? 18 : 0) + (int) Math.ceil(page.getItems().size() / (float) ENTRIES_PER_ROW) * 18;
    }

}
