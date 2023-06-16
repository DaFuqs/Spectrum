package de.dafuqs.spectrum.compat.patchouli.pages;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.resource.language.*;
import net.minecraft.client.util.math.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import vazkii.patchouli.api.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

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