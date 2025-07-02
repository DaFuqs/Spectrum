package de.dafuqs.spectrum.compat.emi.widgets;

import dev.emi.emi.api.widget.*;
import dev.emi.emi.runtime.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;

import java.util.function.*;

public class DynamicTextWidget extends TextWidget {
	
	private static final Minecraft CLIENT = Minecraft.getInstance();
	private final Function<Minecraft, Tuple<FormattedCharSequence, Integer>> textSupplier;
	
	public DynamicTextWidget(Function<Minecraft, Tuple<FormattedCharSequence, Integer>> textSupplier, int x, int y, boolean shadow) {
		super(textSupplier.apply(CLIENT).getA(), x, y, 0, shadow);
		this.textSupplier = textSupplier;
	}
	
	@Override
	public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
		draw.pose().pushPose();
		
		int xOff = horizontalAlignment.offset(CLIENT.font.width(text));
		int yOff = verticalAlignment.offset(CLIENT.font.lineHeight);
		draw.pose().translate(xOff, yOff, 300);
		
		Tuple<FormattedCharSequence, Integer> pair = textSupplier.apply(CLIENT);
		draw.drawString(Minecraft.getInstance().font, pair.getA(), x, y, pair.getB(), shadow);
		
		draw.pose().popPose();
	}
}
