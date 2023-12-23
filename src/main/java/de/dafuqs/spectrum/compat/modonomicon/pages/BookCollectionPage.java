package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.Modonomicon;
import com.klikli_dev.modonomicon.book.BookEntry;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookTextPage;
import com.klikli_dev.modonomicon.util.BookGsonHelper;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconCompat;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BookCollectionPage extends BookTextPage {

    private final List<String> itemStrings;
    private final List<ItemStack> items;

    public BookCollectionPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, List<String> itemStrings) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor);
        this.itemStrings = itemStrings;
        this.items = new ArrayList<>(itemStrings.size());
    }

    public static BookCollectionPage fromJson(JsonObject json) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY);
        var useMarkdownInTitle = JsonHelper.getBoolean(json, "use_markdown_title", false);
        var showTitleSeparator = JsonHelper.getBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var items = JsonHelper.getArray(json, "items", new JsonArray()).asList().stream().map(JsonElement::getAsString).toList();
        return new BookCollectionPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, items);
    }

    public static BookCollectionPage fromNetwork(PacketByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
        var anchor = buffer.readString();
        var items = buffer.readList(PacketByteBuf::readString);
        return new BookCollectionPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, items);
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public Identifier getType() {
        return ModonomiconCompat.COLLECTION_PAGE;
    }

    @Override
    public void build(World world, BookEntry parentEntry, int pageNum) {
        super.build(world, parentEntry, pageNum);

        for (String itemString : itemStrings) {
            try {
                CommandRegistryAccess access = CommandRegistryAccess.of(world.getRegistryManager(), world.getEnabledFeatures());
                ItemStackArgumentType argumentType = new ItemStackArgumentType(access);
                ItemStackArgument argument = argumentType.parse(new StringReader(itemString));
                items.add(argument.createStack(1, false));
            } catch (CommandSyntaxException e) {
                Modonomicon.LOG.warn("Unable to parse stack {} in collection page", itemString);
            }
        }
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeCollection(this.itemStrings, PacketByteBuf::writeString);
    }

    @Override
    public boolean matchesQuery(String query) {
        return super.matchesQuery(query)
                || items.stream()
                .map(ItemStack::getTranslationKey)
                .map(I18n::translate)
                .map(String::toLowerCase)
                .anyMatch(string -> string.contains(query));
    }

}
