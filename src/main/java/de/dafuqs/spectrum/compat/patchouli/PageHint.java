package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.resource.language.*;
import net.minecraft.client.util.math.*;
import net.minecraft.recipe.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import vazkii.patchouli.api.*;
import vazkii.patchouli.client.base.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.common.book.*;

import java.util.*;

public class PageHint extends BookPage {
	
	IVariable cost;
	IVariable text;
	transient BookTextRenderer textRender;
	transient Ingredient ingredient;
	
	// this once was a single property. But because the world sometimes got backdated we have to go this
	// a tad more complicated approach, comparing the current tick with the last reveled tick every time
	transient long lastRevealTick; // advance revealProgress each time this does not match the previous tick
	transient long revealProgress; // -1: not revealed, 0: fully revealed; 1+: currently revealing (+1 every tick)
	
	Text rawText;
	Text displayedText;
	
	String title;
	
	public int getTextHeight() {
		return 22;
	}
	
	@Override
	public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(entry, builder, pageNum);
		ingredient = cost.as(Ingredient.class);
	}
	
	public boolean isQuestDone(Book book) {
		return PersistentData.data.getBookData(book).completedManualQuests.contains(getEntryId());
	}
	
	@Override
	public void onDisplayed(GuiBookEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		rawText = text.as(Text.class);
		
		boolean isDone = isQuestDone(parent.book);
		if (!isDone) {
			revealProgress = -1;
			displayedText = calculateTextToRender(rawText);
			
			PaymentButtonWidget paymentButtonWidget = new PaymentButtonWidget(GuiBook.PAGE_WIDTH / 2 - 50, GuiBook.PAGE_HEIGHT - 35, 100, 20, LiteralText.EMPTY, this::paymentButtonClicked, this);
			addButton(paymentButtonWidget);
		} else {
			displayedText = rawText;
			revealProgress = 0;
		}
		textRender = new BookTextRenderer(parent, displayedText, 0, getTextHeight());
	}
	
	private Text calculateTextToRender(Text text) {
		if (revealProgress == 0) {
			return text;
		} else if (revealProgress < 0) {
			return new LiteralText("$(obf)" + text.getString());
		}
		
		// Show a new letter each tick
		LiteralText calculatedText = new LiteralText(text.asString().substring(0, (int) revealProgress) + "$(obf)" + text.asString().substring((int) revealProgress));
		
		long currentTime = MinecraftClient.getInstance().world.getTime();
		if (currentTime != lastRevealTick) {
			lastRevealTick = currentTime;
			
			revealProgress++;
			revealProgress = Math.min(text.asString().length(), revealProgress);
			if (text.asString().length() < revealProgress) {
				revealProgress = 0;
				return text;
			}
		}
		
		return calculatedText;
	}
	
	protected Identifier getEntryId() {
		return new Identifier(entry.getId().getNamespace(), entry.getId().getPath() + "_" + this.pageNum);
	}
	
	@Environment(EnvType.CLIENT)
	protected void paymentButtonClicked(ButtonWidget button) {
		if (revealProgress > -1) {
			// has already been paid
			return;
		}
		if (MinecraftClient.getInstance().player.isCreative() || InventoryHelper.hasInInventory(List.of(ingredient), MinecraftClient.getInstance().player.getInventory())) {
			// mark as complete in book data
			PersistentData.BookData data = PersistentData.data.getBookData(parent.book);
			data.completedManualQuests.add(getEntryId());
			PersistentData.save();
			entry.markReadStateDirty();
			
			SpectrumClient.minecraftClient.getSoundManager().play(new HintRevelationSoundInstance(mc.player, rawText.asString().length()));
			
			SpectrumC2SPacketSender.sendGuidebookHintBoughtPaket(ingredient);
			revealProgress = 1;
			lastRevealTick = MinecraftClient.getInstance().world.getTime();
			MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		super.render(ms, mouseX, mouseY, pticks);
		
		if (revealProgress >= 0) {
			textRender = new BookTextRenderer(parent, calculateTextToRender(rawText), 0, getTextHeight());
		}
		textRender.render(ms, mouseX, mouseY);
		if (revealProgress == -1) {
			parent.renderIngredient(ms, GuiBook.PAGE_WIDTH / 2 + 23, GuiBook.PAGE_HEIGHT - 34, mouseX, mouseY, ingredient);
		}
		
		parent.drawCenteredStringNoShadow(ms, title == null || title.isEmpty() ? I18n.translate("patchouli.gui.lexicon.objective") : i18n(title), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
		GuiBook.drawSeparator(ms, book, 0, 12);
	}
	
}