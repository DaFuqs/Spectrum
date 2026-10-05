package de.dafuqs.spectrum.inventories.widgets.ink;

import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.network.chat.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;

public class InkListWidget extends AbstractWidget {
	
	protected static final int CHART_HEIGHT = 40;
	protected final int widthPerColor;
	protected final int spaceBetweenColors;
	protected final Collection<InkColor> colors;
	protected final Supplier<InkCapability> inkCapability;
	
	public InkListWidget(int x, int y, Supplier<InkCapability> inkCapability) {
		this(x, y, inkCapability, 4, 2);
	}
	
	public InkListWidget(int x, int y, Supplier<InkCapability> inkCapability, Collection<InkColor> colors) {
		this(x, y, inkCapability, 4, 2, colors);
	}
	
	public InkListWidget(int x, int y, Supplier<InkCapability> inkCapability, int widthPerColor, int spaceBetweenColors) {
		this(x, y, inkCapability, widthPerColor, spaceBetweenColors, inkCapability.get().getStorage().acceptedColors());
	}
	
	public InkListWidget(int x, int y, Supplier<InkCapability> inkCapability, int widthPerColor, int spaceBetweenColors, Collection<InkColor> colors) {
		super(x, y, colors.size() * (widthPerColor + spaceBetweenColors) - spaceBetweenColors, CHART_HEIGHT, Component.empty());
		this.widthPerColor = widthPerColor;
		this.spaceBetweenColors = spaceBetweenColors;
		this.inkCapability = inkCapability;
		this.colors = colors;
	}
	
	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

	}
	
	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		InkStorage inkStorage = inkCapability.get().getStorage();
		drawLines(guiGraphics, inkStorage, colors, getX(), getY());
	}
	
	protected void drawLines(GuiGraphics guiGraphics, InkStorage inkStorage, Collection<InkColor> colors, int x, int y) {
		long maxPerColor = inkStorage.getMaxPerColor();
		int currentX = x;
		for (InkColor color : colors) {
			long amount = inkStorage.getEnergy(color);
			if (amount > 0) {
				int h = Math.max(1, (int) Math.floor(((float) amount / ((float) maxPerColor / CHART_HEIGHT))));
				RenderHelper.fillQuad(guiGraphics.pose(), currentX, y + CHART_HEIGHT - h, h, widthPerColor, color.getColorVec());
			}
			currentX = currentX + widthPerColor + spaceBetweenColors;
		}
	}
	
	@Nullable
	public Tooltip getTooltip() {
		return inkCapability.get().getStorage().getWidgetTooltip();
	}
	
}
