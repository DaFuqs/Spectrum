package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookTextPage;
import com.klikli_dev.modonomicon.util.BookGsonHelper;
import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconCompat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Arrays;

public class BookHintPage extends BookTextPage {

    private final Identifier completionAdvancement;
    private final Ingredient cost;

    public BookHintPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, BookCondition condition, Identifier completionAdvancement,Ingredient cost) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition);
        this.completionAdvancement = completionAdvancement;
        this.cost = cost;
    }

    public static BookHintPage fromJson(JsonObject json) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY);
        var useMarkdownInTitle = JsonHelper.getBoolean(json, "use_markdown_title", false);
        var showTitleSeparator = JsonHelper.getBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(json.getAsJsonObject("condition"))
                : new BookNoneCondition();
        var completionAdvancement = Identifier.tryParse(JsonHelper.getString(json, "completion_advancement"));
        var cost = Ingredient.EMPTY;
        if (json.has("cost")) {
            var ingredient = JsonHelper.getObject(json, "cost");
            var count = JsonHelper.getInt(ingredient, "count", 1);
            cost = Ingredient.fromJson(ingredient);
            Arrays.stream(cost.getMatchingStacks()).forEach(itemStack -> itemStack.setCount(count));
        }
        return new BookHintPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, completionAdvancement, cost);
    }

    public static BookHintPage fromNetwork(PacketByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
        var anchor = buffer.readString();
        var condition = BookCondition.fromNetwork(buffer);
        var completionAdvancement = buffer.readIdentifier();
        var cost = Ingredient.fromPacket(buffer);
        return new BookHintPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, completionAdvancement, cost);
    }

    public Identifier getCompletionAdvancement() {
        return completionAdvancement;
    }

    public Ingredient getCost() {
        return cost;
    }

    public boolean isUnlocked() {
        MinecraftClient client = MinecraftClient.getInstance();
        return AdvancementHelper.hasAdvancement(client.player, completionAdvancement);
    }

    @Override
    public Identifier getType() {
        return ModonomiconCompat.HINT_PAGE;
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeIdentifier(completionAdvancement);
        cost.write(buffer);
    }

    @Override
    public boolean matchesQuery(String query) {
        return isUnlocked()
                ? super.matchesQuery(query)
                : query.toLowerCase().contains("hint");
    }

}
