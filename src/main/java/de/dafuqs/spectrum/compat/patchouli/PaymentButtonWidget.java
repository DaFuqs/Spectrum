package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.InventoryHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import vazkii.patchouli.client.book.gui.GuiBook;

import java.util.List;
import java.util.function.Consumer;

public class PaymentButtonWidget extends ButtonWidget {
	
	PageHint pageHint;
	
	public PaymentButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, PageHint pageHint) {
		super(x, y, width, height, message, onPress);
		this.pageHint = pageHint;
		setMessage(new TranslatableText("spectrum.gui.lexicon.reveal_hint_button.text"));
	}
	
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if(!pageHint.revealed) {
			super.renderButton(matrices, mouseX, mouseY, delta);
			if (this.isHovered()) {
				this.renderTooltip(matrices, mouseX, mouseY);
			}
		}
	}
	
	public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
		if(!pageHint.revealed) {
			this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
		}
	}
	
}
