package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.book.page.*;
import com.klikli_dev.modonomicon.client.gui.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.client.render.page.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.client.gui.components.*;

public class BookConfirmationButtonPageRenderer extends BookTextPageRenderer {

    public BookConfirmationButtonPageRenderer(BookTextPage page) {
        super(page);
    }

    public boolean isConfirmed() {
        if (!(page instanceof BookConfirmationButtonPage confirmationPage)) return false;
        return AdvancementHelper.hasAdvancement(mc.player, confirmationPage.getCheckedAdvancement());
    }

    @Override
    public void onBeginDisplayPage(BookEntryScreen parentScreen, int left, int top) {
        if (!(page instanceof BookConfirmationButtonPage confirmationPage)) return;
        super.onBeginDisplayPage(parentScreen, left, top);

        boolean completed = isConfirmed();

        BookTextHolder buttonText = completed
                ? confirmationPage.getConfirmedButtonText()
                : confirmationPage.getButtonText();
		
		Button button = Button.builder(buttonText.getComponent(), this::confirmationButtonClicked)
				.size(BookEntryScreen.PAGE_WIDTH - 12, Button.DEFAULT_HEIGHT)
				.pos(2, BookEntryScreen.PAGE_HEIGHT - 3)
                .build();

        button.active = !completed;
        addButton(button);
    }
	
	protected void confirmationButtonClicked(Button button) {
        if (!(page instanceof BookConfirmationButtonPage confirmationPage)) return;
        ClientPlayNetworking.send(new GuidebookConfirmationButtonPressedPayload(confirmationPage.getConfirmationString()));
        button.setMessage(confirmationPage.getConfirmedButtonText().getComponent());
        BookGuiManager.get().openEntry(page.getBook().getId(), page.getParentEntry().getId(), page.getPageNumber());
    }

}
