package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.networking.SpectrumC2SPacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class PageConfirmationButton extends PageWithText {
	
	IVariable checked_advancement;
	IVariable button_text;
	IVariable button_text_confirmed;
	IVariable confirmation;
	
	Identifier checkedAdvancementIdentifier;
	Text buttonText;
	Text buttonTextConfirmed;
	String confirmationString;
	
	String title;
	
	@Override
	public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(entry, builder, pageNum);
		
		this.checkedAdvancementIdentifier = Identifier.tryParse(checked_advancement.asString());
		this.buttonText = button_text.as(Text.class);
		this.buttonTextConfirmed = button_text_confirmed.as(Text.class);
		this.confirmationString = confirmation.asString();
	}
	
	public boolean isConfirmed() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, checkedAdvancementIdentifier);
	}
	
	@Override
	public void onDisplayed(GuiBookEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		
		boolean completed = isConfirmed();
		Text buttonText = completed ? buttonTextConfirmed : this.buttonText;
		ButtonWidget button = new ButtonWidget(GuiBook.PAGE_WIDTH / 2 - 50, GuiBook.PAGE_HEIGHT - 35, 100, 20, buttonText, this::confirmationButtonClicked);
		button.active = !isConfirmed();
		addButton(button);
	}
	
	@Override
	public int getTextHeight() {
		return 22;
	}
	
	protected void confirmationButtonClicked(ButtonWidget button) {
		SpectrumC2SPacketSender.sendConfirmationButtonPressedPaket(confirmationString);
		button.setMessage(buttonTextConfirmed);
		entry.markReadStateDirty();
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		super.render(ms, mouseX, mouseY, pticks);
		
		parent.drawCenteredStringNoShadow(ms, title == null || title.isEmpty() ? I18n.translate("patchouli.gui.lexicon.objective") : i18n(title), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
		GuiBook.drawSeparator(ms, book, 0, 12);
		GuiBook.drawSeparator(ms, book, 0, GuiBook.PAGE_HEIGHT - 44);
	}
	
}