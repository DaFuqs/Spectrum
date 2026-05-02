package de.dafuqs.spectrum.inventories.widgets.ink;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.*;

import javax.annotation.Nullable;

public class InkListWidget extends AbstractWidget {
	
	public static final int WIDTH_PER_COLOR = 4;
	public static final int SPACE_BETWEEN_COLORS = 2;
	
	protected final Iterable<InkColor> colors;
	protected final InkStorageBlockEntity<IndividualCappedInkStorage> inkStorageBlockEntity;
	
	public InkListWidget(int x, int y, int height, InkStorageBlockEntity<IndividualCappedInkStorage> inkStorageBlockEntity, Iterable<InkColor> colors) {
		super(x, y, inkStorageBlockEntity.getEnergyStorage().getSupportedColors().size() * (WIDTH_PER_COLOR + SPACE_BETWEEN_COLORS) - SPACE_BETWEEN_COLORS, height, Component.empty());
		this.colors = colors;
		this.inkStorageBlockEntity = inkStorageBlockEntity;
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

	}
	
	@Override
	protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
		int startHeight = getY() + getHeight();
		int currentXOffset = 0;
		
		IndividualCappedInkStorage inkStorage = inkStorageBlockEntity.getEnergyStorage();
		long total = inkStorage.getMaxPerColor();
		for (InkColor color : colors) {
			long amount = inkStorage.getEnergy(color);
			if (amount > 0) {
				int height = Math.max(1, Math.round(((float) amount / ((float) total / getHeight()))));
				RenderHelper.fillQuad(guiGraphics.pose(), getX() + currentXOffset, startHeight - height, height, WIDTH_PER_COLOR, color.getColorVec());
			}
			currentXOffset = currentXOffset + WIDTH_PER_COLOR + SPACE_BETWEEN_COLORS;
		}
	}
	
	@Nullable
	public Tooltip getTooltip() {
		return Tooltip.create(inkStorageBlockEntity.getEnergyStorage().getTooltip());
	}
	
}
