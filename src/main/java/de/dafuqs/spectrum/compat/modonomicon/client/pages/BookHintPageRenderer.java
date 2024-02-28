package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.RenderedBookTextHolder;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookPageRenderer;
import com.klikli_dev.modonomicon.client.render.page.PageWithTextRenderer;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookHintPage;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.networking.SpectrumC2SPacketSender;
import de.dafuqs.spectrum.sound.HintRevelationSoundInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class BookHintPageRenderer extends BookPageRenderer<BookHintPage> implements PageWithTextRenderer {

    public static class PaymentButtonWidget extends ButtonWidget {

        final BookHintPageRenderer pageRenderer;

        public PaymentButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, BookHintPageRenderer pageRenderer) {
            super(x, y, width, height, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
            this.pageRenderer = pageRenderer;
            setMessage(Text.translatable("spectrum.gui.guidebook.reveal_hint_button.text"));
        }

        @Override
        public void renderButton(DrawContext drawContext, int mouseX, int mouseY, float delta) {
            if (pageRenderer.revealProgress < 0) {
                super.renderButton(drawContext, mouseX, mouseY, delta);
            }
        }

    }

    // this once was a single property. But because the world sometimes got backdated we have to go this
    // a tad more complicated approach, comparing the current tick with the last reveled tick every time
    long lastRevealTick; // advance revealProgress each time this does not match the previous tick
    long revealProgress; // -1: not revealed, 0: fully revealed; 1+: currently revealing (+1 every tick)

    public BookHintPageRenderer(BookHintPage page) {
        super(page);
    }

    @Override
    public void onBeginDisplayPage(BookContentScreen parentScreen, int left, int top) {
        super.onBeginDisplayPage(parentScreen, left, top);

        boolean isDone = page.isUnlocked();
        if (!isDone) {
            revealProgress = -1;
//            displayedText = calculateTextToRender(page.getText());

            PaymentButtonWidget paymentButtonWidget = new PaymentButtonWidget(
                    BookContentScreen.PAGE_WIDTH / 2 - 50, BookContentScreen.PAGE_HEIGHT - 35,
                    100, 20, Text.empty(), this::paymentButtonClicked, this);
            addButton(paymentButtonWidget);
        } else {
//            displayedText = page.getText();
            revealProgress = 0;
        }

//        textRender = new BookTextRenderer(parent, displayedText, 0, getTextHeight());
    }

    private BookTextHolder obfuscateText(BookTextHolder text) {
        if (mc.world == null) return BookTextHolder.EMPTY;

        if (revealProgress == 0) {
            return text;
        } else if (revealProgress < 0) {
            if (text.hasComponent()) {
                return new BookTextHolder(text.getComponent().copy().fillStyle(Style.EMPTY.withObfuscated(true)));
            } else if (text instanceof RenderedBookTextHolder renderedText) {
                renderedText.getRenderedText().forEach(mutableText -> mutableText.fillStyle(Style.EMPTY.withObfuscated(true)));
                return text;
            }
        }

        // Show a new letter each tick
//        Text calculatedText = Text.literal(text.getString().substring(0, (int) revealProgress) + "$(obf)" + text.getString().substring((int) revealProgress));

        long currentTime = mc.world.getTime();
        if (currentTime != lastRevealTick) {
            lastRevealTick = currentTime;

            revealProgress++;
//            revealProgress = Math.min(text.getString().length(), revealProgress);
//            if (text.getString().length() < revealProgress) {
//                revealProgress = 0;
//                return text;
//            }
        }

//        return calculatedText;
        return text;
    }

    protected void paymentButtonClicked(ButtonWidget button) {
        if (mc.player == null || mc.world == null) return;
        if (revealProgress > -1) {
            // has already been paid
            return;
        }
        if (mc.player.isCreative() || InventoryHelper.hasInInventory(List.of(page.getCost()), mc.player.getInventory())) {
            MinecraftClient.getInstance().getSoundManager().play(new HintRevelationSoundInstance(mc.player, page.getText().getString().length()));

            SpectrumC2SPacketSender.sendGuidebookHintBoughtPacket(page.getCompletionAdvancement(), page.getCost());
            revealProgress = 1;
            lastRevealTick = mc.world.getTime();
            mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public int getTextY() {
        if (page.hasTitle()) {
            return page.showTitleSeparator() ? 17 : 7;
        }
        return -4;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float ticks) {
        if (page.hasTitle()) {
            renderTitle(drawContext, obfuscateText(page.getTitle()), page.showTitleSeparator(), BookContentScreen.PAGE_WIDTH / 2, 0);
        }

        renderBookTextHolder(drawContext, obfuscateText(page.getText()), 0, getTextY(), BookContentScreen.PAGE_WIDTH);

        if (revealProgress == -1) {
            parentScreen.renderIngredient(drawContext, BookContentScreen.PAGE_WIDTH / 2 + 23, BookContentScreen.PAGE_HEIGHT - 34, mouseX, mouseY, page.getCost());
        }
    }

}
