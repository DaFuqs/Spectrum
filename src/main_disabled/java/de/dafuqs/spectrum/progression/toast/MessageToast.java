package de.dafuqs.spectrum.progression.toast;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.toasts.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class MessageToast implements Toast {
	
	private final ResourceLocation TEXTURE = SpectrumCommon.locate("textures/gui/toasts.png");
	private final ItemStack itemStack;
	private final Component titleText;
	private final Component messageText;
	private final SoundEvent soundEvent;
	private boolean soundPlayed;
	
	public MessageToast(ItemStack itemStack, String text) {
		this.itemStack = itemStack;
		this.soundEvent = SpectrumSoundEvents.NEW_REVELATION;
		this.titleText = Component.translatable("spectrum.toast.message." + text + ".title");
		this.messageText = Component.translatable("spectrum.toast.message." + text + ".text");
		this.soundPlayed = false;
	}
	
	public static void showMessageToast(Minecraft client, ItemStack itemStack, String string) {
		client.getToasts().addToast(new MessageToast(itemStack, string));
	}
	
	@Override
	public Toast.Visibility render(GuiGraphics drawContext, ToastComponent manager, long startTime) {
		drawContext.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());
		
		Minecraft client = manager.getMinecraft();
		Font textRenderer = client.font;
		List<FormattedCharSequence> wrappedText = textRenderer.split(this.messageText, 125);
		List<FormattedCharSequence> wrappedTitle = textRenderer.split(this.titleText, 125);
		int l;
		
		long toastTimeMilliseconds = SpectrumCommon.CONFIG.ToastTimeMilliseconds;
		if (startTime < toastTimeMilliseconds / 2) {
			l = Mth.floor(Mth.clamp((float) (toastTimeMilliseconds / 2 - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
			int halfHeight = this.height() / 2;
			int titleSize = wrappedTitle.size();
			int m = halfHeight - titleSize * 9 / 2;
			
			for (Iterator<FormattedCharSequence> var12 = wrappedTitle.iterator(); var12.hasNext(); m += 9) {
				FormattedCharSequence orderedText = var12.next();
				drawContext.drawString(textRenderer, orderedText, 30, m, RenderHelper.GREEN_COLOR | l, false);
			}
		} else {
			l = Mth.floor(Mth.clamp((float) (startTime - toastTimeMilliseconds / 2) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
			int halfHeight = this.height() / 2;
			int textSize = wrappedText.size();
			int m = halfHeight - textSize * 9 / 2;
			
			for (Iterator<FormattedCharSequence> var12 = wrappedText.iterator(); var12.hasNext(); m += 9) {
				FormattedCharSequence orderedText = var12.next();
				drawContext.drawString(textRenderer, orderedText, 30, m, l, false);
			}
		}
		
		if (!this.soundPlayed && startTime > 0L) {
			this.soundPlayed = true;
			if (this.soundEvent != null) {
				manager.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(this.soundEvent, 1.0F, 0.75F));
			}
		}
		drawContext.renderItem(itemStack, 8, 8);
		return startTime >= toastTimeMilliseconds ? Visibility.HIDE : Visibility.SHOW;
	}
	
}
