package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.page.*;
import com.klikli_dev.modonomicon.util.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.client.resources.language.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

import java.util.*;

public class BookCollectionPage extends BookTextPage {

    private final List<ItemStack> items;
	
	public BookCollectionPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, BookCondition condition, List<ItemStack> stacks) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition);
		this.items = stacks;
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
		List<JsonObject> items = GsonHelper.getAsJsonArray(json, "items", new JsonArray()).asList().stream().map(JsonElement::getAsJsonObject).toList();
		List<ItemStack> stacks = new ArrayList<>();
		for (JsonObject obj : items) {
			try {
				stacks.add(ItemStack.CODEC.parse(provider.createSerializationContext(JsonOps.INSTANCE), obj).getOrThrow());
			} catch (Exception e) {
				SpectrumCommon.logError("Failed parsing item entry " + obj.toString() + " in modonomicon page " + entryId);
				e.printStackTrace();
			}
		}
		return new BookCollectionPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, stacks);
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    @Override
	public ResourceLocation getType() {
        return ModonomiconCompat.COLLECTION_PAGE;
    }

    @Override
	public void toNetwork(RegistryFriendlyByteBuf buffer) {
        super.toNetwork(buffer);
		ItemStack.LIST_STREAM_CODEC.encode(buffer, this.items);
	}
	
	public static BookCollectionPage fromNetwork(RegistryFriendlyByteBuf buffer) {
		var title = BookTextHolder.fromNetwork(buffer);
		var useMarkdownInTitle = buffer.readBoolean();
		var showTitleSeparator = buffer.readBoolean();
		var text = BookTextHolder.fromNetwork(buffer);
		var anchor = buffer.readUtf();
		var condition = BookCondition.fromNetwork(buffer);
		var items = ItemStack.LIST_STREAM_CODEC.decode(buffer);
		return new BookCollectionPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, items);
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
