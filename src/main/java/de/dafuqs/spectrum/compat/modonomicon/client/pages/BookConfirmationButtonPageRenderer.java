package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookTextPage;
import com.klikli_dev.modonomicon.client.gui.BookGuiManager;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookTextPageRenderer;
import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookConfirmationButtonPage;
import de.dafuqs.spectrum.networking.SpectrumC2SPacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;

public class BookConfirmationButtonPageRenderer extends BookTextPageRenderer {

    public BookConfirmationButtonPageRenderer(BookTextPage page) {
        super(page);
    }

    public boolean isConfirmed() {
        if (!(page instanceof BookConfirmationButtonPage confirmationPage)) return false;
        MinecraftClient client = MinecraftClient.getInstance();
        return AdvancementHelper.hasAdvancement(client.player, confirmationPage.getCheckedAdvancement());
    }

    @Override
    public void onBeginDisplayPage(BookContentScreen parentScreen, int left, int top) {
        if (!(page instanceof BookConfirmationButtonPage confirmationPage)) return;
        super.onBeginDisplayPage(parentScreen, left, top);

        boolean completed = isConfirmed();

        BookTextHolder buttonText = completed
                ? confirmationPage.getConfirmedButtonText()
                : confirmationPage.getButtonText();

        ButtonWidget button = ButtonWidget.builder(buttonText.getComponent(), this::confirmationButtonClicked)
                .size(BookContentScreen.PAGE_WIDTH - 12, ButtonWidget.DEFAULT_HEIGHT)
                .position(2, 125)
                .build();

        button.active = !completed;
        addButton(button);
    }

    protected void confirmationButtonClicked(ButtonWidget button) {
        if (!(page instanceof BookConfirmationButtonPage confirmationPage)) return;
        SpectrumC2SPacketSender.sendConfirmationButtonPressedPaket(confirmationPage.getConfirmationString());
        button.setMessage(confirmationPage.getConfirmedButtonText().getComponent());
        BookGuiManager.get().openEntry(page.getBook().getId(), page.getParentEntry().getId(), page.getPageNumber());
    }

}
