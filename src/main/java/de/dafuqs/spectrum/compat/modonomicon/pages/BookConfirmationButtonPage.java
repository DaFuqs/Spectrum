package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookTextPage;
import com.klikli_dev.modonomicon.client.gui.book.markdown.BookTextRenderer;
import com.klikli_dev.modonomicon.util.BookGsonHelper;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconCompat;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class BookConfirmationButtonPage extends BookTextPage {

    private final Identifier checkedAdvancement;
    private BookTextHolder buttonText;
    private BookTextHolder confirmedButtonText;
    private final String confirmationString;

    public BookConfirmationButtonPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, Identifier checkedAdvancement, BookTextHolder buttonText, BookTextHolder confirmedButtonText, String confirmationString) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor);
        this.checkedAdvancement = checkedAdvancement;
        this.buttonText = buttonText;
        this.confirmedButtonText = confirmedButtonText;
        this.confirmationString = confirmationString;
    }

    public static BookConfirmationButtonPage fromJson(JsonObject json) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY);
        var useMarkdownInTitle = JsonHelper.getBoolean(json, "use_markdown_title", false);
        var showTitleSeparator = JsonHelper.getBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var checkedAdvancement = Identifier.tryParse(JsonHelper.getString(json, "checked_advancement"));
        var buttonText = BookGsonHelper.getAsBookTextHolder(json, "button_text", BookTextHolder.EMPTY);
        var confirmedButtonText = BookGsonHelper.getAsBookTextHolder(json, "button_text_confirmed", BookTextHolder.EMPTY);
        var confirmationString = JsonHelper.getString(json, "confirmation", "");
        return new BookConfirmationButtonPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, checkedAdvancement, buttonText, confirmedButtonText, confirmationString);
    }

    public static BookConfirmationButtonPage fromNetwork(PacketByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
        var anchor = buffer.readString();
        var checkedAdvancement = buffer.readIdentifier();
        var buttonText = BookTextHolder.fromNetwork(buffer);
        var confirmedButtonText = BookTextHolder.fromNetwork(buffer);
        var confirmationString = buffer.readString();
        return new BookConfirmationButtonPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, checkedAdvancement, buttonText, confirmedButtonText, confirmationString);
    }

    @Override
    public void prerenderMarkdown(BookTextRenderer textRenderer) {
        super.prerenderMarkdown(textRenderer);

        if (!buttonText.hasComponent()) {
            buttonText = new BookTextHolder(Text.translatable(buttonText.getKey()));
        }

        if (!confirmedButtonText.hasComponent()) {
            confirmedButtonText = new BookTextHolder(Text.translatable(confirmedButtonText.getKey()));
        }
    }

    public Identifier getCheckedAdvancement() {
        return checkedAdvancement;
    }

    public BookTextHolder getButtonText() {
        return buttonText;
    }

    public BookTextHolder getConfirmedButtonText() {
        return confirmedButtonText;
    }

    public String getConfirmationString() {
        return confirmationString;
    }

    @Override
    public Identifier getType() {
        return ModonomiconCompat.CONFIRMATION_BUTTON_PAGE;
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeIdentifier(checkedAdvancement);
        buttonText.toNetwork(buffer);
        confirmedButtonText.toNetwork(buffer);
        buffer.writeString(confirmationString);
    }

}
