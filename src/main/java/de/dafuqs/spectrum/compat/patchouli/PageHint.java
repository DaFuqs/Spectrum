package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.networking.SpectrumC2SPackets;
import de.dafuqs.spectrum.sound.HintRevelationSoundInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.common.book.Book;

import java.util.List;

public class PageHint extends BookPage {
	
	IVariable cost;
	IVariable text;
	transient BookTextRenderer textRender;
	transient Ingredient ingredient;
	
	transient boolean revealed;
	transient long revealTick;
	
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
		
		revealed = isQuestDone(parent.book);
		if(!revealed) {
			revealTick = -1;
			displayedText = calculateTextToRender(rawText);
			
			PaymentButtonWidget paymentButtonWidget = new PaymentButtonWidget(GuiBook.PAGE_WIDTH / 2 - 50, GuiBook.PAGE_HEIGHT - 35, 100, 20, LiteralText.EMPTY, this::paymentButtonClicked, this);
			addButton(paymentButtonWidget);
		} else {
			displayedText = rawText;
			revealTick = 0;
		}
		textRender = new BookTextRenderer(parent, displayedText, 0, getTextHeight());
	}
	
	private Text calculateTextToRender(Text text) {
		if(revealTick == 0) {
			return text;
		}
		
		if(revealTick < 0) {
			return new LiteralText("$(obf)" + text.getString());
		}
		
		long textRevealProgress = MinecraftClient.getInstance().world.getTime() - revealTick;
		if(textRevealProgress > text.asString().length()) {
			revealTick = 0;
			return text;
		} else {
			return new LiteralText(text.asString().substring(0, (int) textRevealProgress) + "$(obf)" + text.asString().substring((int) textRevealProgress));
		}
	}
	
	protected String getEntryId() {
		return entry.getId().toString()+ "_" + this.pageNum;
	}
	
	protected void paymentButtonClicked(ButtonWidget button) {
		if(InventoryHelper.removeFromInventory(List.of(ingredient), MinecraftClient.getInstance().player.getInventory(), true)) {
			// mark as complete in book data
			PersistentData.DataHolder.BookData data = PersistentData.data.getBookData(parent.book);
			data.completedManualQuests.add(getEntryId());
			PersistentData.save();
			entry.markReadStateDirty();
			
			SpectrumClient.minecraftClient.getSoundManager().play(new HintRevelationSoundInstance(mc.player, rawText.asString().length()));
			
			SpectrumC2SPackets.sendGuidebookHintBoughtPaket(ingredient);
			revealed = true;
			revealTick = MinecraftClient.getInstance().world.getTime();
			MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		super.render(ms, mouseX, mouseY, pticks);
		
		if(revealTick > 0) {
			textRender = new BookTextRenderer(parent, calculateTextToRender(rawText), 0, getTextHeight());
		}
		textRender.render(ms, mouseX, mouseY);
		if(!revealed) {
			parent.renderIngredient(ms, GuiBook.PAGE_WIDTH / 2 + 23, GuiBook.PAGE_HEIGHT - 34, mouseX, mouseY, ingredient);
		}
		
		parent.drawCenteredStringNoShadow(ms, title == null || title.isEmpty() ? I18n.translate("patchouli.gui.lexicon.objective") : i18n(title), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
		GuiBook.drawSeparator(ms, book, 0, 12);
	}

}