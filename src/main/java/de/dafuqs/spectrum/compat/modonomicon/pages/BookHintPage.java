package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.Book;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookTextPage;
import com.klikli_dev.modonomicon.util.BookGsonHelper;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconCompat;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class BookHintPage extends BookTextPage {

    private final Ingredient cost;

    public BookHintPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, Ingredient cost) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor);
        this.cost = cost;
    }

    public static BookHintPage fromJson(JsonObject json) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY);
        var useMarkdownInTitle = JsonHelper.getBoolean(json, "use_markdown_title", false);
        var showTitleSeparator = JsonHelper.getBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var cost = json.has("cost") ? Ingredient.fromJson(JsonHelper.getElement(json, "cost")) : Ingredient.EMPTY;
        return new BookHintPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, cost);
    }

    public static BookHintPage fromNetwork(PacketByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
        var anchor = buffer.readString();
        var cost = Ingredient.fromPacket(buffer);
        return new BookHintPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, cost);
    }

    public Ingredient getCost() {
        return cost;
    }

    public boolean isQuestDone(Book book) {
//        return PersistentData.data.getBookData(book).completedManualQuests.contains(parentEntry.getId());
        return false;
    }

    @Override
    public Identifier getType() {
        return ModonomiconCompat.HINT_PAGE;
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        cost.write(buffer);
    }

    @Override
    public boolean matchesQuery(String query) {
        return isQuestDone(book)
                ? super.matchesQuery(query)
                : query.toLowerCase().contains("hint");
    }

}
