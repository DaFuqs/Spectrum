package de.dafuqs.spectrum.compat.patchouli.pages;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.resource.language.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
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
	public void build(World world, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(world, entry, builder, pageNum);
		
		this.checkedAdvancementIdentifier = Identifier.tryParse(checked_advancement.asString());
		this.buttonText = button_text.as(Text.class);
		this.buttonTextConfirmed = button_text_confirmed.as(Text.class);
		this.confirmationString = confirmation.asString();
	}
	
	public boolean isConfirmed() {
		MinecraftClient client = MinecraftClient.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, checkedAdvancementIdentifier);
	}
	
	@Override
	public void onDisplayed(GuiBookEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		
		boolean completed = isConfirmed();
		
		Text buttonText = completed ? buttonTextConfirmed : this.buttonText;
		ButtonWidget button = ButtonWidget.builder(buttonText, this::confirmationButtonClicked)
				.size(GuiBook.PAGE_WIDTH - 12, ButtonWidget.DEFAULT_HEIGHT)
				.position(6, 120)
				.build();
		button.active = !completed;
		
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
	public void render(DrawContext drawContext, int mouseX, int mouseY, float pticks) {
		super.render(drawContext, mouseX, mouseY, pticks);
		
		parent.drawCenteredStringNoShadow(drawContext, title == null || title.isEmpty() ? I18n.translate("patchouli.gui.lexicon.objective") : i18n(title), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
		GuiBook.drawSeparator(drawContext, book, 0, 12);
		GuiBook.drawSeparator(drawContext, book, 0, GuiBook.PAGE_HEIGHT - 44);
	}
	
}
