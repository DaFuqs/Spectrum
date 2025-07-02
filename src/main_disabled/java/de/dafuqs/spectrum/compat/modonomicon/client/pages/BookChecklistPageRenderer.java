package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.client.render.page.*;
import com.klikli_dev.modonomicon.data.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;

import java.util.*;

public class BookChecklistPageRenderer extends BookTextPageRenderer {

    public BookChecklistPageRenderer(BookChecklistPage page) {
        super(page);
    }

    @Override
    public void onBeginDisplayPage(BookEntryScreen parentScreen, int left, int top) {
        if (!(page instanceof BookChecklistPage checklistPage)) return;
        if (!(page.getText() instanceof RenderedBookTextHolder renderedText)) return;

        super.onBeginDisplayPage(parentScreen, left, top);
		
		List<MutableComponent> renderedTexts = renderedText.getRenderedText();
		
		ResourceLocation font = BookDataManager.Client.get().safeFont(this.page.getBook().getFont());
        
        int i = 0;
		for (Map.Entry<ResourceLocation, BookTextHolder> entry : checklistPage.getChecklist().entrySet()) {
            boolean hasAchievement = AdvancementHelper.hasAdvancementClient(entry.getKey());
			renderedTexts.get(i).withStyle(Style.EMPTY.withStrikethrough(hasAchievement).withFont(font));
			List<Component> siblings = renderedTexts.get(i).getSiblings();
            siblings.removeLast();
			siblings.add(Component.literal(hasAchievement ? " ✔" : ""));
            i++;
        }
    }

}
