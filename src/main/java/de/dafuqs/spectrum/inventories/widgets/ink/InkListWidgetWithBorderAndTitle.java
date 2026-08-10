package de.dafuqs.spectrum.inventories.widgets.ink;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

import java.util.*;
import java.util.function.*;

public class InkListWidgetWithBorderAndTitle extends InkListWidget {
	
	protected static final ResourceLocation BACKGROUND_SPRITE = SpectrumCommon.locate("widget/ink_list");
	protected static final ResourceLocation INK_SPRITE = SpectrumCommon.locate("widget/ink_text");
	protected static final int TITLE_HEIGHT = 18;
	
	public InkListWidgetWithBorderAndTitle(int x, int y, int height, Supplier<InkCapability> inkCapability) {
		super(x, y, height, inkCapability, 4, 1, 5);
	}
	
	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		// We first collect the colors we need to draw
		// this widget does skip ink colors that the player has no access to yet
		InkStorage inkStorage = inkCapability.get().getStorage();
		List<Tuple<InkColor, Long>> inkColorAmountsOverZero = new ArrayList<>();
		for (InkColor color : colors) {
			long amount = inkStorage.getEnergy(color);
			if(amount > 0) {
			inkColorAmountsOverZero.add(new Tuple<>(color, amount));}
		}
		
		int prevWidth = getWidth();
		int newWidth = Math.max(45, inkColorAmountsOverZero.size() * (widthPerColor + spaceBetweenColors) + padding + padding);
		setWidth(newWidth);
		setX(getX() + prevWidth - newWidth);
		
		// background and title
		// we exceed the widgets size here so the mouseover still only shows up when hovering the gauges
		guiGraphics.blitSprite(BACKGROUND_SPRITE, this.getX(), this.getY() - TITLE_HEIGHT, this.getWidth(), this.getHeight() + TITLE_HEIGHT);
		guiGraphics.blitSprite(INK_SPRITE, this.getX() + 4, this.getY() - 12, 40, 14);
		
		// foreground bars
		int startHeight = getY() + getHeight() + padding;
		int currentXOffset = 0;
		long total = inkStorage.getMaxPerColor();
		for (Tuple<InkColor, Long> entry : inkColorAmountsOverZero) {
			int height = Math.max(1, Math.round(((float) entry.getB() / ((float) total / getHeight()))));
			RenderHelper.fillQuad(guiGraphics.pose(), getX() + currentXOffset + padding, startHeight - height, height - padding - padding, widthPerColor, entry.getA().getColorVec());
			currentXOffset = currentXOffset + widthPerColor + spaceBetweenColors;
		}
	}
	
}
