package de.dafuqs.spectrum.compat.patchouli.pages;

import com.google.gson.annotations.*;
import de.dafuqs.revelationary.api.advancements.*;
import net.minecraft.client.util.math.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import vazkii.patchouli.api.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PageChecklist extends BookPage {
	
	protected String title;
	transient Text titleText;
	
	protected IVariable text;
	transient BookTextRenderer textRender;
	
	@SerializedName("checklist")
	final
	Map<String, String> checklist = new HashMap<>();
	
	@Override
	public void onDisplayed(GuiBookEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		
		StringBuilder stringBuilder = new StringBuilder();
		if (text == null) {
			text = IVariable.wrap("");
		} else {
			stringBuilder.append(text.asString());
		}
		
		for (Map.Entry<String, String> entry : checklist.entrySet()) {
			String value = entry.getValue();
			if (AdvancementHelper.hasAdvancementClient(Identifier.tryParse(entry.getKey()))) {
				stringBuilder.append("$(li)");
				stringBuilder.append("$(m)");
				stringBuilder.append(value);
				stringBuilder.append("$()");
				stringBuilder.append(" ✔");
			} else {
				stringBuilder.append("$(li)");
				stringBuilder.append(value);
				stringBuilder.append("$()");
			}
		}
		
		textRender = new BookTextRenderer(parent, IVariable.wrap(stringBuilder.toString()).as(Text.class), 0, getTextHeight());
		
		if (title == null) {
			title = "";
			titleText = Text.literal("");
		} else {
			titleText = i18nText(title);
		}
	}
	
	public int getTextHeight() {
		return title == null ? -4 : title.isEmpty() ? -4 : 14;
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		super.render(ms, mouseX, mouseY, pticks);
		
		textRender.render(ms, mouseX, mouseY);
		parent.drawCenteredStringNoShadow(ms, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
	}
	
	protected Text getTitle() {
		return titleText;
	}
	
}