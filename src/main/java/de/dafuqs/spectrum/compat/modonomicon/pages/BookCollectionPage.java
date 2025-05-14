package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.*;
import com.klikli_dev.modonomicon.*;
import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.entries.*;
import com.klikli_dev.modonomicon.book.page.*;
import com.klikli_dev.modonomicon.util.*;
import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.client.resources.language.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.item.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class BookCollectionPage extends BookTextPage {

    private final List<String> itemStrings;
    private final List<ItemStack> items;

    public BookCollectionPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, BookCondition condition, List<String> itemStrings) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition);
        this.itemStrings = itemStrings;
        this.items = new ArrayList<>(itemStrings.size());
    }
	
	public static BookCollectionPage fromJson(ResourceLocation entryId, JsonObject json, HolderLookup.Provider provider) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY, provider);
		var useMarkdownInTitle = GsonHelper.getAsBoolean(json, "use_markdown_title", false);
		var showTitleSeparator = GsonHelper.getAsBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY, provider);
		var anchor = GsonHelper.getAsString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(entryId, json.getAsJsonObject("condition"), provider)
                : new BookNoneCondition();
		var items = GsonHelper.getAsJsonArray(json, "items", new JsonArray()).asList().stream().map(JsonElement::getAsString).toList();
        return new BookCollectionPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, items);
    }
	
	public static BookCollectionPage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
		var anchor = buffer.readUtf();
        var condition = BookCondition.fromNetwork(buffer);
		var items = buffer.readList(FriendlyByteBuf::readUtf);
        return new BookCollectionPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, items);
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    @Override
	public ResourceLocation getType() {
        return ModonomiconCompat.COLLECTION_PAGE;
    }

    @Override
	public void build(Level world, BookContentEntry parentEntry, int pageNum) {
        super.build(world, parentEntry, pageNum);

        for (String itemString : itemStrings) {
            try {
				CommandBuildContext access = CommandBuildContext.simple(world.registryAccess(), world.enabledFeatures());
				ItemArgument argumentType = new ItemArgument(access);
				ItemInput argument = argumentType.parse(new StringReader(itemString));
				items.add(argument.createItemStack(1, false));
            } catch (CommandSyntaxException e) {
                Modonomicon.LOG.warn("Unable to parse stack {} in collection page", itemString);
            }
        }
    }

    @Override
	public void toNetwork(RegistryFriendlyByteBuf buffer) {
        super.toNetwork(buffer);
		buffer.writeCollection(this.itemStrings, FriendlyByteBuf::writeUtf);
    }

    @Override
    public boolean matchesQuery(String query) {
        return super.matchesQuery(query)
                || items.stream()
				.map(ItemStack::getDescriptionId)
				.map(I18n::get)
                .map(String::toLowerCase)
                .anyMatch(string -> string.contains(query));
    }

}
