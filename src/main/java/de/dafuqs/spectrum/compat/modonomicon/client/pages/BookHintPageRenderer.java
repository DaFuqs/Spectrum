package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.*;
import com.klikli_dev.modonomicon.client.render.page.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BookHintPageRenderer extends BookPageRenderer<BookHintPage> implements PageWithTextRenderer {
    
    private Style OBFUSCATED_STYLE;
    private HintRevelationSoundInstance soundInstance;

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

    @Nullable
    private BookTextHolder obfuscatedText;

    // this once was a single property. But because the world sometimes got backdated we have to go this
    // a tad more complicated approach, comparing the current time with the last reveled time every tick
    long lastRevealTime = 0; // advance revealProgress each time this does not match the previous tick
    int revealProgress; // -1: not revealed, 0: fully revealed; 1+: currently revealing (+1 every tick)

    public BookHintPageRenderer(BookHintPage page) {
        super(page);
    }

    @Override
    public void onBeginDisplayPage(BookContentScreen parentScreen, int left, int top) {
        super.onBeginDisplayPage(parentScreen, left, top);
        
        OBFUSCATED_STYLE = Style.EMPTY
                .withColor(TextColor.fromRgb(0x000000))
                .withBold(false)
                .withItalic(false)
                .withUnderline(false)
                .withStrikethrough(false)
                .withObfuscated(true)
                .withClickEvent(null)
                .withHoverEvent(null)
                .withInsertion(null)
                .withFont(page.getBook().getFont());

        obfuscatedText = null;
        
        boolean isDone = AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, page.getCompletionAdvancement());
        if (!isDone) {
            revealProgress = -1;

            PaymentButtonWidget paymentButtonWidget = new PaymentButtonWidget(
                    2, BookContentScreen.PAGE_HEIGHT - 3,
                    BookContentScreen.PAGE_WIDTH - 12, ButtonWidget.DEFAULT_HEIGHT,
                    Text.empty(), this::paymentButtonClicked, this);
            addButton(paymentButtonWidget);
        } else {
            revealProgress = 0;
        }
    }

    private BookTextHolder splitObfuscateText(BookTextHolder text) {
        if (text.hasComponent()) {
            List<MutableText> newText = new ArrayList<>(1);
            newText.add(splitObfuscateText(text.getComponent().copy()));
            return new RenderedBookTextHolder(text, newText);
        } else if (text instanceof RenderedBookTextHolder renderedText) {
            List<MutableText> newRenderedText = new ArrayList<>(renderedText.getRenderedText().size());
            for (MutableText mutableText : renderedText.getRenderedText()) {
                newRenderedText.add(splitObfuscateText(mutableText));
            }
            return new RenderedBookTextHolder(text, newRenderedText);
        }
        return BookTextHolder.EMPTY;
    }

    private MutableText splitObfuscateText(MutableText text) {
        MutableText out = Text.empty();
        text.asOrderedText().accept((index, style, codepoint) -> {
            String charStr = String.valueOf((char) codepoint);
            out.append(Text.empty()
                    .append(Text.literal(charStr).setStyle(OBFUSCATED_STYLE))
                    .setStyle(style));
            return true;
        });
        return out;
    }

    private MutableText floodStyle(Text text, Style style) {
        MutableText out = MutableText.of(text.getContent()).setStyle(style);
        for (Text sibling : text.getSiblings()) {
            out.append(floodStyle(sibling, style));
        }
        return out;
    }

    private BookTextHolder obfuscateText(BookTextHolder text, @Nullable BookTextHolder splitText, int start) {
        if (mc.world == null) return BookTextHolder.EMPTY;

        if (revealProgress == 0) {
            return text;
        } else if (revealProgress == -1) {
            if (splitText != null) return splitText;
            if (text.hasComponent()) {
                return new BookTextHolder(floodStyle(text.getComponent(), OBFUSCATED_STYLE));
            } else if (text instanceof RenderedBookTextHolder renderedText) {

                List<MutableText> mutableTexts = renderedText.getRenderedText().stream()
                        .map(mutableText -> floodStyle(mutableText, OBFUSCATED_STYLE))
                        .toList();
                return new RenderedBookTextHolder(renderedText, mutableTexts);
            }
        }

        if (revealProgress == 1 || revealProgress + 1 == start) {
            splitText = splitObfuscateText(text);
        }

        if (revealProgress < start) return splitText;

        if (splitText instanceof RenderedBookTextHolder renderedText) {
            int c = 0;
            List<MutableText> mutableTexts = renderedText.getRenderedText();
            for (int i = 0; i < mutableTexts.size(); i++) {
                MutableText mutableText = mutableTexts.get(i);

                if (c + mutableText.getSiblings().size() > revealProgress - start) {
                    MutableText newMutableText = Text.empty();
                    for (int s = 0; s < mutableText.getSiblings().size(); s++) {
                        Text sibling = mutableText.getSiblings().get(s);
                        newMutableText.append(c + s == revealProgress - start && sibling.getSiblings().size() == 1
                                ? sibling.getSiblings().get(0).copy().setStyle(sibling.getStyle())
                                : sibling);
                    }
                    mutableTexts.set(i, newMutableText);
                    break;
                }

                c += mutableText.getSiblings().size();
            }
        }

        return splitText;
    }

    private boolean isDoneRevealing(BookTextHolder obfText) {
        if (obfText instanceof RenderedBookTextHolder renderedText) {
            var mutableTexts = renderedText.getRenderedText();
            var lastText = mutableTexts.get(mutableTexts.size() - 1);
            var siblings = lastText.getSiblings();
            var lastSibling = siblings.get(siblings.size() - 1);
            return lastSibling.getSiblings().isEmpty();
        }
        return true;
    }

    protected void paymentButtonClicked(ButtonWidget button) {
        if (mc.player == null || mc.world == null) return;
        if (revealProgress > -1) {
            // has already been paid
            return;
        }
        if (mc.player.isCreative() || InventoryHelper.hasInInventory(List.of(page.getCost()), mc.player.getInventory())) {
            soundInstance = new HintRevelationSoundInstance(mc.player);
            MinecraftClient.getInstance().getSoundManager().play(soundInstance);

            SpectrumC2SPacketSender.sendGuidebookHintBoughtPacket(page.getCompletionAdvancement(), page.getCost());
            revealProgress = 1;
            lastRevealTime = mc.world.getTime();
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
        if (mc.world == null) return;
        
        renderTitle(drawContext, this.page.getTitle(), page.showTitleSeparator(), BookContentScreen.PAGE_WIDTH / 2, 0);

        int textStart = 1;

        obfuscatedText = obfuscateText(page.getText(), obfuscatedText, textStart);
        renderBookTextHolder(drawContext, obfuscatedText, 0, getTextY(), BookContentScreen.PAGE_WIDTH);

        var style = this.getClickedComponentStyleAt(mouseX, mouseY);
        if (style != null)
            this.parentScreen.renderComponentHoverEffect(drawContext, style, mouseX, mouseY);

        if (revealProgress == -1) {
            parentScreen.renderIngredient(drawContext, BookContentScreen.PAGE_WIDTH / 2 + 29, BookContentScreen.PAGE_HEIGHT - 3, mouseX, mouseY, page.getCost());
        }

        if (revealProgress > 0) {
            long currentTime = System.currentTimeMillis() / 20;
            
            if (isDoneRevealing(obfuscatedText)) {
                soundInstance.setDone();
                revealProgress = 0;
                obfuscatedText = null;
            } else if (currentTime != lastRevealTime) {
                lastRevealTime = currentTime;
                revealProgress++;
            }
        }
    }

    @Nullable
    @Override
    public Style getClickedComponentStyleAt(double pMouseX, double pMouseY) {
        if (pMouseX > 0 && pMouseY > 0) {
            if (this.page.hasTitle()) {
                var titleStyle = this.getClickedComponentStyleAtForTitle(this.page.getTitle(), BookContentScreen.PAGE_WIDTH / 2, 0, pMouseX, pMouseY);
                if (titleStyle != null) {
                    return titleStyle;
                }
            }

            var textStyle = this.getClickedComponentStyleAtForTextHolder(obfuscatedText, 0, this.getTextY(), BookContentScreen.PAGE_WIDTH, pMouseX, pMouseY);
            if (textStyle != null) {
                return textStyle;
            }
        }
        return super.getClickedComponentStyleAt(pMouseX, pMouseY);
    }

}
