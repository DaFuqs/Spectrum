package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookTextPage;
import com.klikli_dev.modonomicon.client.gui.book.markdown.BookTextRenderer;
import com.klikli_dev.modonomicon.util.BookGsonHelper;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconCompat;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class BookStatusEffectPage extends BookTextPage {

    private final Identifier statusEffectId;

    public BookStatusEffectPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, Identifier statusEffectId) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor);
        this.statusEffectId = statusEffectId;
    }

    public static BookStatusEffectPage fromJson(JsonObject json) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY);
        var useMarkdownInTitle = JsonHelper.getBoolean(json, "use_markdown_title", false);
        var showTitleSeparator = JsonHelper.getBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var statusEffectId = json.has("status_effect_id") ? Identifier.tryParse(JsonHelper.getString(json, "status_effect_id")) : null;
        return new BookStatusEffectPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, statusEffectId);
    }

    public static BookStatusEffectPage fromNetwork(PacketByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
        var anchor = buffer.readString();
        var statusEffectId = buffer.readIdentifier();
        return new BookStatusEffectPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, statusEffectId);
    }

    @Override
    public Identifier getType() {
        return ModonomiconCompat.STATUS_EFFECT_PAGE;
    }

    public Identifier getStatusEffectId() {
        return this.statusEffectId;
    }

    @Override
    public void prerenderMarkdown(BookTextRenderer textRenderer) {
        if (title.isEmpty()) {
            title = new BookTextHolder(statusEffectId.toTranslationKey("effect"));
        }

        super.prerenderMarkdown(textRenderer);
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeIdentifier(this.statusEffectId);
    }

}
