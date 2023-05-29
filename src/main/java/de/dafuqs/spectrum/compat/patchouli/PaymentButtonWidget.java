package de.dafuqs.spectrum.compat.patchouli;

import net.minecraft.client.gui.widget.*;
import net.minecraft.client.util.math.*;
import net.minecraft.text.*;

public class PaymentButtonWidget extends ButtonWidget {
	
	final PageHint pageHint;
	
	public PaymentButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, PageHint pageHint) {
		super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
		this.pageHint = pageHint;
		setMessage(Text.translatable("spectrum.gui.lexicon.reveal_hint_button.text"));
	}
	
	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (pageHint.revealProgress < 0) {
			super.renderButton(matrices, mouseX, mouseY, delta);
			if (this.isHovered()) {
				// TODO - Is this number correct here?
				this.drawTooltip(matrices, mouseX, mouseY, 0.1f, delta);
			}
		}
	}
	
}
