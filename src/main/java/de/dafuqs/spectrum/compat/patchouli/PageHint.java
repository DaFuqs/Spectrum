package de.dafuqs.spectrum.compat.patchouli;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import vazkii.patchouli.client.base.ClientAdvancements;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.page.abstr.PageWithText;
import vazkii.patchouli.common.book.Book;

public class PageHint extends PageWithText {
	
	Identifier trigger;
	String title;
	
	public PageHint() {
		super();
	}
	
	@Override
	public int getTextHeight() {
		return 22;
	}
	
	@Override
	public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(entry, builder, pageNum);
		trigger = null;
	}
	
	public boolean isCompleted(Book book) {
		return PersistentData.data.getBookData(book).completedManualQuests.contains(entry.getId().toString());
	}
	
	@Override
	public void onDisplayed(GuiBookEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		
		boolean completed = isCompleted(parent.book);
		if(!completed) {
			ButtonWidget button = new ButtonWidget(GuiBook.PAGE_WIDTH / 2 - 50, GuiBook.PAGE_HEIGHT - 35, 100, 20, LiteralText.EMPTY, this::questButtonClicked);
			addButton(button);
			button.setMessage(new TranslatableText("spectrum.gui.lexicon.reveal_hint"));
		}
	}
	
	protected void questButtonClicked(ButtonWidget button) {
		String res = entry.getId().toString();
		PersistentData.DataHolder.BookData data = PersistentData.data.getBookData(parent.book);
		
		if (data.completedManualQuests.contains(res)) {
			data.completedManualQuests.remove(res);
		} else {
			data.completedManualQuests.add(res);
		}
		PersistentData.save();
		entry.markReadStateDirty();
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		super.render(ms, mouseX, mouseY, pticks);
		
		parent.drawCenteredStringNoShadow(ms, title == null || title.isEmpty() ? I18n.translate("patchouli.gui.lexicon.objective") : i18n(title), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
		GuiBook.drawSeparator(ms, book, 0, 12);
	}

}