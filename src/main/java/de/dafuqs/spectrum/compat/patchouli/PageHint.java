package de.dafuqs.spectrum.compat.patchouli;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.page.PageSpotlight;
import vazkii.patchouli.common.book.Book;

public class PageHint extends BookPage {
	
	IVariable cost;
	IVariable text;
	transient BookTextRenderer textRender;
	transient Ingredient ingredient;
	transient boolean revealed;
	
	String title;
	
	public int getTextHeight() {
		return 22;
	}
	
	@Override
	public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(entry, builder, pageNum);
		ingredient = cost.as(Ingredient.class);
	}
	
	public boolean isRevealed(Book book) {
		return PersistentData.data.getBookData(book).completedManualQuests.contains(entry.getId().toString());
	}
	
	@Override
	public void onDisplayed(GuiBookEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		Text displayedText = text.as(Text.class);

		revealed = isRevealed(parent.book);
		if(!revealed) {
			ButtonWidget button = new ButtonWidget(GuiBook.PAGE_WIDTH / 2 - 50, GuiBook.PAGE_HEIGHT - 35, 100, 20, LiteralText.EMPTY, this::revealHintButtonClicked);
			addButton(button);
			button.setMessage(new TranslatableText("spectrum.gui.lexicon.reveal_hint"));
			displayedText = new LiteralText("$(obf)" + displayedText.getString());
		}
		textRender = new BookTextRenderer(parent, displayedText, 0, getTextHeight());
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		return textRender.click(mouseX, mouseY, mouseButton);
	}
	
	protected void revealHintButtonClicked(ButtonWidget button) {
		String res = entry.getId().toString();
		PersistentData.DataHolder.BookData data = PersistentData.data.getBookData(parent.book);
		
		if (data.completedManualQuests.contains(res)) {
			data.completedManualQuests.remove(res);
		} else {
			data.completedManualQuests.add(res);
		}
		
		Text displayedText = text.as(Text.class);
		textRender = new BookTextRenderer(parent, displayedText, 0, getTextHeight());
		
		// TODO: pay cost
		// TODO: reveal cloaked entry
		// TODO: give player "used hint" advancement
		
		PersistentData.save();
		entry.markReadStateDirty();
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		super.render(ms, mouseX, mouseY, pticks);
		
		textRender.render(ms, mouseX, mouseY);
		if(!revealed) {
			parent.renderIngredient(ms, GuiBook.PAGE_WIDTH / 2 + 23, GuiBook.PAGE_HEIGHT - 34, mouseX, mouseY, ingredient);
		}
		
		parent.drawCenteredStringNoShadow(ms, title == null || title.isEmpty() ? I18n.translate("patchouli.gui.lexicon.objective") : i18n(title), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
		GuiBook.drawSeparator(ms, book, 0, 12);
	}

}