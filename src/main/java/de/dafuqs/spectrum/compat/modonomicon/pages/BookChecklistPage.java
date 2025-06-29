package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.common.collect.*;
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

import java.util.*;

public class BookChecklistPage extends BookTextPage {
	
	private final int startCountingAt;
	private final Map<ResourceLocation, BookTextHolder> checklist;
	
	public BookChecklistPage(BookTextHolder title, BookTextHolder text, boolean useMarkdownInTitle, boolean showTitleSeparator, String anchor, BookCondition condition, Map<ResourceLocation, BookTextHolder> checklist, int startCountingAt) {
        super(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition);
        this.checklist = checklist;
		this.startCountingAt = startCountingAt;
    }
	
	public static BookChecklistPage fromJson(ResourceLocation entryId, JsonObject json, HolderLookup.Provider provider) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY, provider);
		var useMarkdownInTitle = GsonHelper.getAsBoolean(json, "use_markdown_title", false);
		var showTitleSeparator = GsonHelper.getAsBoolean(json, "show_title_separator", true);
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY, provider);
		var anchor = GsonHelper.getAsString(json, "anchor", "");
		int startCountingAt = GsonHelper.getAsInt(json, "start_counting_at", 1);
		var condition = json.has("condition")
                ? BookCondition.fromJson(entryId, json.getAsJsonObject("condition"), provider)
                : new BookNoneCondition();
		var checklistObject = GsonHelper.getAsJsonObject(json, "checklist", new JsonObject());
		Map<ResourceLocation, BookTextHolder> checklist = new LinkedHashMap<>();
        for (var key : checklistObject.keySet()) {
            var value = BookGsonHelper.getAsBookTextHolder(checklistObject, key, BookTextHolder.EMPTY, provider);
			checklist.put(ResourceLocation.parse(key), value);
        }
		return new BookChecklistPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, checklist, startCountingAt);
    }
	
	public static BookChecklistPage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var useMarkdownInTitle = buffer.readBoolean();
        var showTitleSeparator = buffer.readBoolean();
        var text = BookTextHolder.fromNetwork(buffer);
		var anchor = buffer.readUtf();
        var condition = BookCondition.fromNetwork(buffer);

        // Because modonomicon decided RegistryByteBuf was worthwhile
        int checklistSize = buffer.readVarInt();
		Map<ResourceLocation, BookTextHolder> checklist = Maps.newLinkedHashMapWithExpectedSize(checklistSize);
        for(int i = 0; i < checklistSize; i++) {
			var key = buffer.readResourceLocation();
            var value = BookTextHolder.fromNetwork(buffer);
            checklist.put(key, value);
        }
		int startCountingAt = buffer.readInt();
		
		return new BookChecklistPage(title, text, useMarkdownInTitle, showTitleSeparator, anchor, condition, checklist, startCountingAt);
    }
	
	public Map<ResourceLocation, BookTextHolder> getChecklist() {
        return checklist;
    }

    @Override
    public void prerenderMarkdown(BookTextRenderer textRenderer) {
        super.prerenderMarkdown(textRenderer);
		
		List<MutableComponent> mutableTexts = new ArrayList<>();
		
		int i = startCountingAt;
		for (Map.Entry<ResourceLocation, BookTextHolder> entry : checklist.entrySet()) {
            BookTextHolder entryText = entry.getValue();
			List<MutableComponent> rendered = textRenderer.render(entryText.getString());
			
			MutableComponent parent = Component.literal(String.format("%d. ", i));
			for (MutableComponent mutableText : rendered) {
                parent.append(mutableText);
            }
			parent.append(Component.literal(""));

            mutableTexts.add(parent);
            i++;
        }

        if (text instanceof RenderedBookTextHolder renderedText) {
            mutableTexts.addAll(renderedText.getRenderedText());
        } else {
            mutableTexts.add(text.getComponent().copy());
        }

        text = new RenderedBookTextHolder(new BookTextHolder(""), mutableTexts);
    }

    @Override
	public ResourceLocation getType() {
        return ModonomiconCompat.CHECKLIST_PAGE;
    }

    @Override
	public void toNetwork(RegistryFriendlyByteBuf buffer) {
        super.toNetwork(buffer);
		
		buffer.writeVarInt(checklist.size());
		for (var entry : checklist.entrySet()) {
			buffer.writeResourceLocation(entry.getKey());
			entry.getValue().toNetwork(buffer);
		}
		buffer.writeVarInt(startCountingAt);
    }

}
