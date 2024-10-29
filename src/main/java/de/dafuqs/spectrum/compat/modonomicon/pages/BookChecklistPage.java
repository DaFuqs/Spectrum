package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.common.collect.*;
import com.google.gson.*;
import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.page.*;
import com.klikli_dev.modonomicon.client.gui.book.markdown.*;
import com.klikli_dev.modonomicon.util.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class BookChecklistPage extends BookTextPage {

    private final Map<Identifier, BookTextHolder> checklist;

    public BookChecklistPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, BookCondition condition, Map<Identifier, BookTextHolder> checklist) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition);
        this.checklist = checklist;
    }

    public static BookChecklistPage fromJson(JsonObject json) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY);
        var useMarkdownInTitle = JsonHelper.getBoolean(json, "use_markdown_title", false);
        var showTitleSeparator = JsonHelper.getBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(json.getAsJsonObject("condition"))
                : new BookNoneCondition();
        var checklistObject = JsonHelper.getObject(json, "checklist", new JsonObject());
        Map<Identifier, BookTextHolder> checklist = new LinkedHashMap<>();
        for (var key : checklistObject.keySet()) {
            var value = BookGsonHelper.getAsBookTextHolder(checklistObject, key, BookTextHolder.EMPTY);
            checklist.put(new Identifier(key), value);
        }
        return new BookChecklistPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, checklist);
    }

    public static BookChecklistPage fromNetwork(PacketByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
        var anchor = buffer.readString();
        var condition = BookCondition.fromNetwork(buffer);
        var checklist = buffer.readMap(Maps::newLinkedHashMapWithExpectedSize, PacketByteBuf::readIdentifier, BookTextHolder::fromNetwork);
        return new BookChecklistPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, checklist);
    }

    public Map<Identifier, BookTextHolder> getChecklist() {
        return checklist;
    }

    @Override
    public void prerenderMarkdown(BookTextRenderer textRenderer) {
        super.prerenderMarkdown(textRenderer);

        List<MutableText> mutableTexts = new ArrayList<>();

        int i = 1;
        for (Map.Entry<Identifier, BookTextHolder> entry : checklist.entrySet()) {
            BookTextHolder entryText = entry.getValue();
            List<MutableText> rendered = textRenderer.render(entryText.getString());

            MutableText parent = Text.literal(String.format("%d. ", i));
            for (MutableText mutableText : rendered) {
                parent.append(mutableText);
            }
            parent.append(Text.literal(""));

            mutableTexts.add(parent);
            i++;
        }

        if (text instanceof RenderedBookTextHolder renderedText) {
            mutableTexts.addAll(renderedText.getRenderedText());
        } else {
            mutableTexts.add(text.getComponent().copy());
        }

        text = new RenderedBookTextHolder(new BookTextHolder(""), mutableTexts);
    }

    @Override
    public Identifier getType() {
        return ModonomiconCompat.CHECKLIST_PAGE;
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeMap(checklist, PacketByteBuf::writeIdentifier, (buf, value) -> value.toNetwork(buf));
    }

}
