package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.client.render.page.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.*;
import org.jspecify.annotations.*;

public class BookLinkPageRenderer extends BookTextPageRenderer {
	
	private static final int BUTTON_HEIGHT = Button.DEFAULT_HEIGHT;
	private static final int BUTTON_WIDTH = BookEntryScreen.PAGE_WIDTH - 12;
	private static final int BUTTON_X = 2;
	private static final int BUTTON_Y = BookEntryScreen.PAGE_HEIGHT - BUTTON_HEIGHT - 3;
	
	public BookLinkPageRenderer(BookLinkPage page) {
		super(page);
	}
	
	@Override
	public void onBeginDisplayPage(BookEntryScreen parentScreen, int left, int top) {
		if (!(page instanceof BookLinkPage linkPage)) return;
		
		super.onBeginDisplayPage(parentScreen, left, top);
		
		addButton(Button.builder(linkPage.getLinkText().getComponent(), (b) -> {
				})
				.size(BUTTON_WIDTH, BUTTON_HEIGHT)
				.pos(BUTTON_X, BUTTON_Y)
				.build());
	}

	@Override
	public @Nullable Style getClickedComponentStyleAt(double pMouseX, double pMouseY) {
		if (!(page instanceof BookLinkPage linkPage)) return null;
		
		if (pMouseX >= BUTTON_X && pMouseY >= BUTTON_Y && pMouseX < BUTTON_X + BUTTON_WIDTH && pMouseY < BUTTON_Y + BUTTON_HEIGHT) {
			return linkPage.getLinkText().getComponent().getStyle();
		}
		
		return super.getClickedComponentStyleAt(pMouseX, pMouseY);
	}
	
}
