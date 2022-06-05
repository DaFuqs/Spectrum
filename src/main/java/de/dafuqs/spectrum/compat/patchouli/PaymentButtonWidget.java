package de.dafuqs.spectrum.compat.patchouli;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PaymentButtonWidget extends ButtonWidget {
	
	PageHint pageHint;
	
	public PaymentButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, PageHint pageHint) {
		super(x, y, width, height, message, onPress);
		this.pageHint = pageHint;
		setMessage(new TranslatableText("spectrum.gui.lexicon.reveal_hint_button.text"));
	}
	
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (pageHint.revealProgress < 0) {
			super.renderButton(matrices, mouseX, mouseY, delta);
			if (this.isHovered()) {
				this.renderTooltip(matrices, mouseX, mouseY);
			}
		}
	}
	
}
