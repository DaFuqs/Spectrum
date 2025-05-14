package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.client.render.page.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BookHintPageRenderer extends BookPageRenderer<BookHintPage> implements PageWithTextRenderer {
    
    private Style OBFUSCATED_STYLE;
    private HintRevelationSoundInstance soundInstance;
	
	public static class PaymentButtonWidget extends Button {

        final BookHintPageRenderer pageRenderer;
		
		public PaymentButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress, BookHintPageRenderer pageRenderer) {
			super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
            this.pageRenderer = pageRenderer;
			setMessage(Component.translatable("spectrum.gui.guidebook.reveal_hint_button.text"));
        }

        @Override
		protected void renderWidget(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
            if (pageRenderer.revealProgress < 0) {
                super.renderWidget(drawContext, mouseX, mouseY, delta);
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
    public void onBeginDisplayPage(BookEntryScreen parentScreen, int left, int top) {
        super.onBeginDisplayPage(parentScreen, left, top);
        
        OBFUSCATED_STYLE = Style.EMPTY
                .withColor(TextColor.fromRgb(0x000000))
                .withBold(false)
                .withItalic(false)
				.withUnderlined(false)
                .withStrikethrough(false)
                .withObfuscated(true)
                .withClickEvent(null)
                .withHoverEvent(null)
                .withInsertion(null)
                .withFont(page.getBook().getFont());

        obfuscatedText = null;
		
		boolean isDone = AdvancementHelper.hasAdvancement(Minecraft.getInstance().player, page.getCompletionAdvancement());
        if (!isDone) {
            revealProgress = -1;
			
			addButton(new PaymentButtonWidget(
					2, BookEntryScreen.PAGE_HEIGHT - Button.DEFAULT_HEIGHT - 2,
					BookEntryScreen.PAGE_WIDTH - 12, Button.DEFAULT_HEIGHT,
					Component.empty(), this::paymentButtonClicked, this));
        } else {
            revealProgress = 0;
        }
    }

    private BookTextHolder splitObfuscateText(BookTextHolder text) {
        if (text.hasComponent()) {
			List<MutableComponent> newText = new ArrayList<>(1);
            newText.add(splitObfuscateText(text.getComponent().copy()));
            return new RenderedBookTextHolder(text, newText);
        } else if (text instanceof RenderedBookTextHolder renderedText) {
			List<MutableComponent> newRenderedText = new ArrayList<>(renderedText.getRenderedText().size());
			for (MutableComponent mutableText : renderedText.getRenderedText()) {
                newRenderedText.add(splitObfuscateText(mutableText));
            }
            return new RenderedBookTextHolder(text, newRenderedText);
        }
        return BookTextHolder.EMPTY;
    }
	
	private MutableComponent splitObfuscateText(MutableComponent text) {
		MutableComponent out = Component.empty();
		text.getVisualOrderText().accept((index, style, codepoint) -> {
            String charStr = String.valueOf((char) codepoint);
			out.append(Component.empty()
					.append(Component.literal(charStr).setStyle(OBFUSCATED_STYLE))
                    .setStyle(style));
            return true;
        });
        return out;
    }
	
	private MutableComponent floodStyle(Component text, Style style) {
		MutableComponent out = MutableComponent.create(text.getContents()).setStyle(style);
		for (Component sibling : text.getSiblings()) {
            out.append(floodStyle(sibling, style));
        }
        return out;
    }

    private BookTextHolder obfuscateText(BookTextHolder text, @Nullable BookTextHolder splitText, int start) {
		if (mc.level == null) return BookTextHolder.EMPTY;

        if (revealProgress == 0) {
            return text;
        } else if (revealProgress == -1) {
            if (splitText != null) return splitText;
            if (text.hasComponent()) {
                return new BookTextHolder(floodStyle(text.getComponent(), OBFUSCATED_STYLE));
            } else if (text instanceof RenderedBookTextHolder renderedText) {
				
				List<MutableComponent> mutableTexts = renderedText.getRenderedText().stream()
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
			List<MutableComponent> mutableTexts = renderedText.getRenderedText();
            for (int i = 0; i < mutableTexts.size(); i++) {
				MutableComponent mutableText = mutableTexts.get(i);

                if (c + mutableText.getSiblings().size() > revealProgress - start) {
					MutableComponent newMutableText = Component.empty();
                    for (int s = 0; s < mutableText.getSiblings().size(); s++) {
						Component sibling = mutableText.getSiblings().get(s);
                        newMutableText.append(c + s == revealProgress - start && sibling.getSiblings().size() == 1
                                ? sibling.getSiblings().getFirst().copy().setStyle(sibling.getStyle())
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
            var lastText = mutableTexts.getLast();
            var siblings = lastText.getSiblings();
            var lastSibling = siblings.getLast();
            return lastSibling.getSiblings().isEmpty();
        }
        return true;
    }
	
	protected void paymentButtonClicked(Button button) {
		if (mc.player == null || mc.level == null) return;
        if (revealProgress > -1) {
            // has already been paid
            return;
        }
		
		if (mc.player.isCreative() || InventoryHelper.hasIngredientStacksInInventory(List.of(page.getCost()), mc.player.getInventory())) {
            soundInstance = new HintRevelationSoundInstance(mc.player);
			Minecraft.getInstance().getSoundManager().play(soundInstance);
            
            ClientPlayNetworking.send(new GuidebookHintBoughtPayload(page.getCompletionAdvancement(), page.getCost()));
            revealProgress = 1;
			lastRevealTime = mc.level.getGameTime();
			mc.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
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
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float ticks) {
		if (mc.level == null) return;
        
        renderTitle(drawContext, this.page.getTitle(), page.showTitleSeparator(), BookEntryScreen.PAGE_WIDTH / 2, 0);

        int textStart = 1;

        obfuscatedText = obfuscateText(page.getText(), obfuscatedText, textStart);
        renderBookTextHolder(drawContext, obfuscatedText, 0, getTextY(), BookEntryScreen.PAGE_WIDTH);

        var style = this.getClickedComponentStyleAt(mouseX, mouseY);
        if (style != null)
            this.parentScreen.renderComponentHoverEffect(drawContext, style, mouseX, mouseY);

        if (revealProgress == -1) {
			ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, BookEntryScreen.PAGE_WIDTH / 2 + 29, BookEntryScreen.PAGE_HEIGHT - Button.DEFAULT_HEIGHT - 1, mouseX, mouseY, page.getCost());
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
                var titleStyle = this.getClickedComponentStyleAtForTitle(this.page.getTitle(), BookEntryScreen.PAGE_WIDTH / 2, 0, pMouseX, pMouseY);
                if (titleStyle != null) {
                    return titleStyle;
                }
            }

            var textStyle = this.getClickedComponentStyleAtForTextHolder(obfuscatedText, 0, this.getTextY(), BookEntryScreen.PAGE_WIDTH, pMouseX, pMouseY);
            if (textStyle != null) {
                return textStyle;
            }
        }
        return super.getClickedComponentStyleAt(pMouseX, pMouseY);
    }

}
