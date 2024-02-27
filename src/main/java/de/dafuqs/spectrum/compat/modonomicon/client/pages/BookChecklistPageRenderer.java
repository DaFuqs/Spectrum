package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.RenderedBookTextHolder;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookTextPageRenderer;
import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookChecklistPage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class BookChecklistPageRenderer extends BookTextPageRenderer {

    public BookChecklistPageRenderer(BookChecklistPage page) {
        super(page);
    }

    @Override
    public void onBeginDisplayPage(BookContentScreen parentScreen, int left, int top) {
        if (!(page instanceof BookChecklistPage checklistPage)) return;
        if (!(page.getText() instanceof RenderedBookTextHolder renderedText)) return;

        super.onBeginDisplayPage(parentScreen, left, top);

        List<MutableText> renderedTexts = renderedText.getRenderedText();

        int i = 0;
        for (Map.Entry<Identifier, BookTextHolder> entry : checklistPage.getChecklist().entrySet()) {
            boolean hasAchievement = AdvancementHelper.hasAdvancementClient(entry.getKey());
            renderedTexts.get(i).fillStyle(Style.EMPTY.withStrikethrough(hasAchievement));
            List<Text> siblings = renderedTexts.get(i).getSiblings();
            siblings.remove(siblings.size()-1);
            siblings.add(Text.literal(hasAchievement ? " ✔" : ""));
            i++;
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float ticks) {
        if (!(page.getText() instanceof RenderedBookTextHolder renderedText)) return;

        if (page.hasTitle()) {
            this.renderTitle(drawContext, page.getTitle(), page.showTitleSeparator(), BookContentScreen.PAGE_WIDTH / 2, 0);
        }

        renderBookTextHolder(drawContext, renderedText, 0, super.getTextY(), BookContentScreen.PAGE_WIDTH);

        var style = getClickedComponentStyleAt(mouseX, mouseY);
        if (style != null)
            parentScreen.renderComponentHoverEffect(drawContext, style, mouseX, mouseY);
    }
}
