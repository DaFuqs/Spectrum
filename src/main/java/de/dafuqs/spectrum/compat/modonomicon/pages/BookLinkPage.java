package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookTextPage;
import com.klikli_dev.modonomicon.client.gui.book.markdown.BookTextRenderer;
import com.klikli_dev.modonomicon.util.BookGsonHelper;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconCompat;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class BookLinkPage extends BookTextPage {

    private final String url;
    private BookTextHolder linkText;

    public BookLinkPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, BookCondition condition, String url, BookTextHolder linkText) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition);
        this.url = url;
        this.linkText = linkText;
    }

    public static BookLinkPage fromJson(JsonObject json) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY);
        var useMarkdownInTitle = JsonHelper.getBoolean(json, "use_markdown_title", false);
        var showTitleSeparator = JsonHelper.getBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(json.getAsJsonObject("condition"))
                : new BookNoneCondition();
        var url = JsonHelper.getString(json, "url", "");
        var linkText = BookGsonHelper.getAsBookTextHolder(json, "link_text", BookTextHolder.EMPTY);;
        return new BookLinkPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, url, linkText);
    }

    public static BookLinkPage fromNetwork(PacketByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
        var anchor = buffer.readString();
        var condition = BookCondition.fromNetwork(buffer);
        var url = buffer.readString();
        var linkText = BookTextHolder.fromNetwork(buffer);
        return new BookLinkPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, url, linkText);
    }

    public BookTextHolder getLinkText() {
        return linkText;
    }

    @Override
    public Identifier getType() {
        return ModonomiconCompat.LINK_PAGE;
    }

    @Override
    public void prerenderMarkdown(BookTextRenderer textRenderer) {
        super.prerenderMarkdown(textRenderer);

        if (!linkText.hasComponent()) {
            MutableText text = Text.translatable(linkText.getKey());
            Style style = Style.EMPTY
                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of(url)));
            linkText = new BookTextHolder(text.fillStyle(style));
        }
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeString(url);
        this.linkText.toNetwork(buffer);
    }

    @Override
    public boolean matchesQuery(String query) {
        return super.matchesQuery(query)
                || url.toLowerCase().contains(query)
                || linkText.getString().toLowerCase().contains(query);
    }

}
