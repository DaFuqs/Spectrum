package de.dafuqs.spectrum.compat.modonomicon.page_types;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.book.entries.*;
import com.klikli_dev.modonomicon.book.error.*;
import com.klikli_dev.modonomicon.client.gui.book.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

/**
 * A node that is a link to another existing entry
 * similar to com.klikli_dev.modonomicon.book.entries.CategoryLinkBookEntry,
 * but for entries, instead of a category
 */
public class EntryLinkBookEntry extends BookEntry {
	
	/**
	 * The entry to open on click
	 */
	protected Identifier entryToOpenId;
	protected BookEntry entryToOpen;
	
	public EntryLinkBookEntry(Identifier id, BookEntryData data, Identifier commandToRunOnFirstReadId, Identifier entryToOpenId) {
		super(id, data, commandToRunOnFirstReadId);
		this.entryToOpenId = entryToOpenId;
	}
	
	@Override
	public Identifier getType() {
		return ModonomiconCompat.ENTRY_LINK_PAGE_TYPE;
	}
	
	public static EntryLinkBookEntry fromJson(Identifier id, JsonObject json, boolean autoAddReadConditions) {
		BookEntryData data = BookEntryData.fromJson(json, autoAddReadConditions);
		
		Identifier commandToRunOnFirstReadId = null;
		if (json.has("command_to_run_on_first_read")) {
			commandToRunOnFirstReadId = new Identifier(JsonHelper.getString(json, "command_to_run_on_first_read"));
		}
		Identifier entryToOpen = new Identifier(JsonHelper.getString(json, "entry_to_open"));
		
		return new EntryLinkBookEntry(id, data, commandToRunOnFirstReadId, entryToOpen);
	}
	
	@Override
	public void toNetwork(PacketByteBuf buffer) {
		buffer.writeIdentifier(this.id);
		this.data.toNetwork(buffer);
		buffer.writeNullable(this.commandToRunOnFirstReadId, PacketByteBuf::writeIdentifier);
		buffer.writeNullable(this.entryToOpenId, PacketByteBuf::writeIdentifier);
	}
	
	public static EntryLinkBookEntry fromNetwork(PacketByteBuf buffer) {
		var id = buffer.readIdentifier();
		BookEntryData data = BookEntryData.fromNetwork(buffer);
		Identifier commandToRunOnFirstReadId = buffer.readNullable(PacketByteBuf::readIdentifier);
		Identifier entryToOpen = buffer.readNullable(PacketByteBuf::readIdentifier);
		
		return new EntryLinkBookEntry(id, data, commandToRunOnFirstReadId, entryToOpen);
	}
	
	@Override
	public void build(World level, BookCategory category) {
		super.build(level, category);
		
		if (this.entryToOpenId != null) {
			this.entryToOpen = this.getBook().getEntry(this.entryToOpenId);
			
			if (this.entryToOpen == null) {
				BookErrorManager.get().error("Entry to open \"" + this.entryToOpenId + "\" does not exist in this book. Set to null.");
				this.entryToOpenId = null;
			}
		}
	}
	
	public BookEntry getEntryToOpen() {
		return this.entryToOpen;
	}
	
	public BookContentScreen openEntry(BookCategoryScreen categoryScreen) {
		categoryScreen.openEntry(getEntryToOpen());
		return null;
	}
	
}
