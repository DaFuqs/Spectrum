package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.page.*;
import com.klikli_dev.modonomicon.client.gui.book.markdown.*;
import com.klikli_dev.modonomicon.util.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

public class BookLinkPage extends BookTextPage {
	
	private final String url;
	private BookTextHolder linkText;
	
	public BookLinkPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, BookCondition condition, String url, BookTextHolder linkText) {
		super(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition);
		this.url = url;
		this.linkText = linkText;
	}
	
	public static BookLinkPage fromJson(ResourceLocation entryId, JsonObject json, HolderLookup.Provider provider) {
		var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY, provider);
		var useMarkdownInTitle = GsonHelper.getAsBoolean(json, "use_markdown_title", false);
		var showTitleSeparator = GsonHelper.getAsBoolean(json, "show_title_separator", true);
		var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY, provider);
		var anchor = GsonHelper.getAsString(json, "anchor", "");
		var condition = json.has("condition")
				? BookCondition.fromJson(entryId, json.getAsJsonObject("condition"), provider)
				: new BookNoneCondition();
		var url = GsonHelper.getAsString(json, "url", "");
		var linkText = BookGsonHelper.getAsBookTextHolder(json, "link_text", BookTextHolder.EMPTY, provider);
		return new BookLinkPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, url, linkText);
	}
	
	public static BookLinkPage fromNetwork(RegistryFriendlyByteBuf buffer) {
		var title = BookTextHolder.fromNetwork(buffer);
		var useMarkdownInTitle = buffer.readBoolean();
		var showTitleSeparator = buffer.readBoolean();
		var text = BookTextHolder.fromNetwork(buffer);
		var anchor = buffer.readUtf();
		var condition = BookCondition.fromNetwork(buffer);
		var url = buffer.readUtf();
		var linkText = BookTextHolder.fromNetwork(buffer);
		return new BookLinkPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, url, linkText);
	}
	
	public BookTextHolder getLinkText() {
		return linkText;
	}
	
	@Override
	public ResourceLocation getType() {
		return ModonomiconCompat.LINK_PAGE;
	}
	
	@Override
	public void prerenderMarkdown(BookTextRenderer textRenderer) {
		super.prerenderMarkdown(textRenderer);
		
		if (!linkText.hasComponent()) {
			MutableComponent text = Component.translatable(linkText.getKey());
			Style style = Style.EMPTY
					.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
					.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.nullToEmpty(url)));
			linkText = new BookTextHolder(text.withStyle(style));
		}
	}
	
	@Override
	public void toNetwork(RegistryFriendlyByteBuf buffer) {
		super.toNetwork(buffer);
		buffer.writeUtf(url);
		this.linkText.toNetwork(buffer);
	}
	
	@Override
	public boolean matchesQuery(String query) {
		return super.matchesQuery(query)
				|| url.toLowerCase().contains(query)
				|| linkText.getString().toLowerCase().contains(query);
	}
	
}
