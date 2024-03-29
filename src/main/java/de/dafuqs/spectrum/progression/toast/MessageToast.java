package de.dafuqs.spectrum.progression.toast;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.font.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.sound.*;
import net.minecraft.client.toast.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class MessageToast implements Toast {
	
	private final Identifier TEXTURE = SpectrumCommon.locate("textures/gui/toasts.png");
	private final ItemStack itemStack;
	private final Text titleText;
	private final Text messageText;
	private final SoundEvent soundEvent;
	private boolean soundPlayed;
	
	public MessageToast(ItemStack itemStack, String text) {
		this.itemStack = itemStack;
		this.soundEvent = SpectrumSoundEvents.NEW_REVELATION;
		this.titleText = Text.translatable("spectrum.toast.message." + text + ".title");
		this.messageText = Text.translatable("spectrum.toast.message." + text + ".text");
		this.soundPlayed = false;
	}
	
	public static void showMessageToast(MinecraftClient client, ItemStack itemStack, String string) {
		client.getToastManager().add(new MessageToast(itemStack, string));
	}
	
	@Override
	public Toast.Visibility draw(DrawContext drawContext, ToastManager manager, long startTime) {
		drawContext.drawTexture(TEXTURE, 0, 0, 0, 0, this.getWidth(), this.getHeight());
		
		MinecraftClient client = manager.getClient();
		TextRenderer textRenderer = client.textRenderer;
		List<OrderedText> wrappedText = textRenderer.wrapLines(this.messageText, 125);
		List<OrderedText> wrappedTitle = textRenderer.wrapLines(this.titleText, 125);
		int l;
		
		long toastTimeMilliseconds = SpectrumCommon.CONFIG.ToastTimeMilliseconds;
		if (startTime < toastTimeMilliseconds / 2) {
			l = MathHelper.floor(MathHelper.clamp((float) (toastTimeMilliseconds / 2 - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
			int halfHeight = this.getHeight() / 2;
			int titleSize = wrappedTitle.size();
			int m = halfHeight - titleSize * 9 / 2;
			
			for (Iterator<OrderedText> var12 = wrappedTitle.iterator(); var12.hasNext(); m += 9) {
				OrderedText orderedText = var12.next();
				drawContext.drawText(textRenderer, orderedText, 30, m, RenderHelper.GREEN_COLOR | l, false);
			}
		} else {
			l = MathHelper.floor(MathHelper.clamp((float) (startTime - toastTimeMilliseconds / 2) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
			int halfHeight = this.getHeight() / 2;
			int textSize = wrappedText.size();
			int m = halfHeight - textSize * 9 / 2;
			
			for (Iterator<OrderedText> var12 = wrappedText.iterator(); var12.hasNext(); m += 9) {
				OrderedText orderedText = var12.next();
				drawContext.drawText(textRenderer, orderedText, 30, m, l, false);
			}
		}
		
		if (!this.soundPlayed && startTime > 0L) {
			this.soundPlayed = true;
			if (this.soundEvent != null) {
				manager.getClient().getSoundManager().play(PositionedSoundInstance.master(this.soundEvent, 1.0F, 0.75F));
			}
		}
		drawContext.drawItem(itemStack, 8, 8);
		return startTime >= toastTimeMilliseconds ? Visibility.HIDE : Visibility.SHOW;
	}
	
}
