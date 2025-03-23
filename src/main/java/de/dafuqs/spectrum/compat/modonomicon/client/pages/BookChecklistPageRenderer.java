package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.*;
import com.klikli_dev.modonomicon.client.render.page.*;
import com.klikli_dev.modonomicon.data.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

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
        
        Identifier font = BookDataManager.Client.get().safeFont(this.page.getBook().getFont());
        
        int i = 0;
        for (Map.Entry<Identifier, BookTextHolder> entry : checklistPage.getChecklist().entrySet()) {
            boolean hasAchievement = AdvancementHelper.hasAdvancementClient(entry.getKey());
            renderedTexts.get(i).fillStyle(Style.EMPTY.withStrikethrough(hasAchievement).withFont(font));
            List<Text> siblings = renderedTexts.get(i).getSiblings();
            siblings.remove(siblings.size()-1);
            siblings.add(Text.literal(hasAchievement ? " ✔" : ""));
            i++;
        }
    }

}
