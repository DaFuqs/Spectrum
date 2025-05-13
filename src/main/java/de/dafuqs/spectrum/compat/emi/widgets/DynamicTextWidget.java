package de.dafuqs.spectrum.compat.emi.widgets;

import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.function.*;

public class DynamicTextWidget extends TextWidget {
	
	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
	private final Function<MinecraftClient, Pair<OrderedText, Integer>> textSupplier;
	
	public DynamicTextWidget(Function<MinecraftClient, Pair<OrderedText, Integer>> textSupplier, int x, int y, boolean shadow) {
		super(textSupplier.apply(CLIENT).getLeft(), x, y, 0, shadow);
		this.textSupplier = textSupplier;
	}
	
	@Override
	public void render(DrawContext draw, int mouseX, int mouseY, float delta) {
		draw.getMatrices().push();
		int xOff = horizontalAlignment.offset(CLIENT.textRenderer.getWidth(text));
		int yOff = verticalAlignment.offset(CLIENT.textRenderer.fontHeight);
		draw.getMatrices().translate(xOff, yOff, 300);
		
		var pair = textSupplier.apply(CLIENT);
		
		if (shadow) {
			draw.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, pair.getLeft(), x, y, pair.getRight());
		} else {
			draw.drawText(MinecraftClient.getInstance().textRenderer, pair.getLeft(), x, y, pair.getRight(), false);
		}
		draw.getMatrices().pop();
	}
}
