package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.page.*;
import com.klikli_dev.modonomicon.util.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

public class BookHintPage extends BookTextPage {
	
	private final ResourceLocation completionAdvancement;
	private final IngredientStack cost;
	
	public BookHintPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, BookCondition condition, ResourceLocation completionAdvancement, IngredientStack cost) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition);
        this.completionAdvancement = completionAdvancement;
        this.cost = cost;
    }
	
	public static BookHintPage fromJson(ResourceLocation entryId, JsonObject json, HolderLookup.Provider provider) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY, provider);
		var useMarkdownInTitle = GsonHelper.getAsBoolean(json, "use_markdown_title", false);
		var showTitleSeparator = GsonHelper.getAsBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY, provider);
		var anchor = GsonHelper.getAsString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(entryId, json.getAsJsonObject("condition"), provider)
                : new BookNoneCondition();
		var completionAdvancement = ResourceLocation.parse(GsonHelper.getAsString(json, "completion_advancement"));
		IngredientStack cost = IngredientStack.EMPTY;
        if (json.has("cost")) {
			var ingredient = GsonHelper.getAsJsonObject(json, "cost");
			cost = IngredientStack.Serializer.CODEC.parse(provider.createSerializationContext(JsonOps.INSTANCE), ingredient).result().orElse(cost);
        }
        return new BookHintPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, completionAdvancement, cost);
    }
	
	public static BookHintPage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
		var anchor = buffer.readUtf();
        var condition = BookCondition.fromNetwork(buffer);
		var completionAdvancement = buffer.readResourceLocation();
		var cost = IngredientStack.Serializer.PACKET_CODEC.decode(buffer);
        return new BookHintPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, completionAdvancement, cost);
    }
	
	public ResourceLocation getCompletionAdvancement() {
        return completionAdvancement;
    }
	
	public IngredientStack getCost() {
        return cost;
    }

    @Override
	public ResourceLocation getType() {
        return ModonomiconCompat.HINT_PAGE;
    }

    @Override
	public void toNetwork(RegistryFriendlyByteBuf buffer) {
        super.toNetwork(buffer);
		buffer.writeResourceLocation(completionAdvancement);
		IngredientStack.Serializer.PACKET_CODEC.encode(buffer, cost);
    }

}
