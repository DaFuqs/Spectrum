package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookTextPage;
import com.klikli_dev.modonomicon.util.BookGsonHelper;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconCompat;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class BookSnippetPage extends BookTextPage {

    private final Identifier resourcePath;
    private final int resourceWidth;
    private final int resourceHeight;
    private final int textureX;
    private final int textureY;
    private final int textureWidth;
    private final int textureHeight;

    public BookSnippetPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, BookCondition condition, Identifier resourcePath, int resourceWidth, int resourceHeight, int textureX, int textureY, int textureWidth, int textureHeight) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition);
        this.resourcePath = resourcePath;
        this.resourceWidth = resourceWidth;
        this.resourceHeight = resourceHeight;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public static BookSnippetPage fromJson(JsonObject json) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY);
        var useMarkdownInTitle = JsonHelper.getBoolean(json, "use_markdown_title", false);
        var showTitleSeparator = JsonHelper.getBoolean(json, "show_title_separator", false);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(json.getAsJsonObject("condition"))
                : new BookNoneCondition();
        var resourcePath = Identifier.tryParse(JsonHelper.getString(json, "resource_path"));
        var resourceWidth = JsonHelper.getInt(json, "resource_width");
        var resourceHeight = JsonHelper.getInt(json, "resource_height");
        var textureX = JsonHelper.getInt(json, "texture_x");
        var textureY = JsonHelper.getInt(json, "texture_y");
        var textureWidth = JsonHelper.getInt(json, "texture_width");
        var textureHeight = JsonHelper.getInt(json, "texture_height");
        return new BookSnippetPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, resourcePath, resourceWidth, resourceHeight, textureX, textureY, textureWidth, textureHeight);
    }

    public static BookSnippetPage fromNetwork(PacketByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
        var anchor = buffer.readString();
        var condition = BookCondition.fromNetwork(buffer);
        var resourcePath = buffer.readIdentifier();
        var resourceWidth = buffer.readVarInt();
        var resourceHeight = buffer.readVarInt();
        var textureX = buffer.readVarInt();
        var textureY = buffer.readVarInt();
        var textureWidth = buffer.readVarInt();
        var textureHeight = buffer.readVarInt();
        return new BookSnippetPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, resourcePath, resourceWidth, resourceHeight, textureX, textureY, textureWidth, textureHeight);
    }

    public Identifier getResourcePath() {
        return resourcePath;
    }

    public int getResourceWidth() {
        return resourceWidth;
    }

    public int getResourceHeight() {
        return resourceHeight;
    }

    public int getTextureX() {
        return textureX;
    }

    public int getTextureY() {
        return textureY;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }


    @Override
    public Identifier getType() {
        return ModonomiconCompat.SNIPPET_PAGE;
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeIdentifier(resourcePath);
        buffer.writeVarInt(resourceWidth);
        buffer.writeVarInt(resourceHeight);
        buffer.writeVarInt(textureX);
        buffer.writeVarInt(textureY);
        buffer.writeVarInt(textureWidth);
        buffer.writeVarInt(textureHeight);
    }

}
