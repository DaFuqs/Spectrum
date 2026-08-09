package de.dafuqs.spectrum.inventories.widgets.ink;

import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.*;

import javax.annotation.Nullable;
import java.util.function.*;

public class InkListWidget extends AbstractWidget {
	
	protected final int padding;
	protected final int widthPerColor;
	protected final int spaceBetweenColors;
	protected final Iterable<InkColor> colors;
	protected final Supplier<InkCapability> inkCapability;
	
	public InkListWidget(int x, int y, int height, Supplier<InkCapability> inkCapability) {
		this(x, y, height, inkCapability, 4, 2, 0);
	}
	
	public InkListWidget(int x, int y, int height, Supplier<InkCapability> inkCapability, int widthPerColor, int spaceBetweenColors, int padding) {
		super(x, y, inkCapability.get().getStorage().acceptedColors().size() * (widthPerColor + spaceBetweenColors) - spaceBetweenColors + padding + padding, height, Component.empty());
		this.widthPerColor = widthPerColor;
		this.padding = padding;
		this.spaceBetweenColors = spaceBetweenColors;
		this.inkCapability = inkCapability;
		this.colors = inkCapability.get().getStorage().acceptedColors();
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

	}
	
	@Override
	protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		InkStorage inkStorage = inkCapability.get().getStorage();
		
		int startHeight = getY() + getHeight() + padding;
		int currentXOffset = 0;
		long total = inkStorage.getMaxPerColor();
		for (InkColor color : colors) {
			long amount = inkStorage.getEnergy(color);
			if (amount > 0) {
				int height = Math.max(1, Math.round(((float) amount / ((float) total / getHeight()))));
				RenderHelper.fillQuad(guiGraphics.pose(), getX() + currentXOffset + padding, startHeight - height, height - padding - padding, widthPerColor, color.getColorVec());
			}
			currentXOffset = currentXOffset + widthPerColor + spaceBetweenColors;
		}
	}
	
	@Nullable
	public Tooltip getTooltip() {
		return inkCapability.get().getStorage().getWidgetTooltip();
	}
	
}
