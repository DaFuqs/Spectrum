package de.dafuqs.spectrum.compat.modonomicon.page_types;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.entries.*;
import com.klikli_dev.modonomicon.client.gui.book.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

/**
 * A node that is a link to another existing entry
 * similar to com.klikli_dev.modonomicon.book.entries.CategoryLinkBookEntry,
 * but for entries, instead of a category
 */
public class WebLinkEntry extends BookEntry {
	
	private final String url;
	
	public WebLinkEntry(ResourceLocation id, BookEntryData data, ResourceLocation commandToRunOnFirstReadId, String url) {
		super(id, data, commandToRunOnFirstReadId);
		this.url = url;
	}
	
	@Override
	public ResourceLocation getType() {
		return ModonomiconCompat.WEB_LINK_ENTRY_TYPE;
	}
	
	@Override
	public void openEntry(BookAddress bookAddress) {
		ConfirmLinkScreen.confirmLinkNow(Minecraft.getInstance().screen, url, false);
	}
	
	@Override
	public boolean matchesQuery(String query) {
		return false;
	}
	
	@Override
	public void toNetwork(RegistryFriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.id);
		this.data.toNetwork(buffer);
		buffer.writeNullable(this.commandToRunOnFirstReadId, FriendlyByteBuf::writeResourceLocation);
		buffer.writeUtf(url);
	}
	
	public static WebLinkEntry fromNetwork(RegistryFriendlyByteBuf buffer) {
		var id = buffer.readResourceLocation();
		BookEntryData data = BookEntryData.fromNetwork(buffer);
		ResourceLocation commandToRunOnFirstReadId = buffer.readNullable(FriendlyByteBuf::readResourceLocation);
		var url = buffer.readUtf();
		
		return new WebLinkEntry(id, data, commandToRunOnFirstReadId, url);
	}
	
	public static WebLinkEntry fromJson(ResourceLocation id, JsonObject json, boolean autoAddReadConditions, HolderLookup.Provider wrapperLookup) {
		BookEntry.BookEntryData data = BookEntryData.fromJson(id, json, autoAddReadConditions, wrapperLookup);
		ResourceLocation commandToRunOnFirstReadId = null;
		if (json.has("command_to_run_on_first_read")) {
			commandToRunOnFirstReadId = ResourceLocation.parse(GsonHelper.getAsString(json, "command_to_run_on_first_read"));
		}
		String url = GsonHelper.getAsString(json, "url");
		return new WebLinkEntry(id, data, commandToRunOnFirstReadId, url);
	}
}