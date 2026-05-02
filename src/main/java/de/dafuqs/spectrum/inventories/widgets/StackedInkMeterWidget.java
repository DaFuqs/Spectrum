package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.*;

import java.util.*;


public class StackedInkMeterWidget extends AbstractWidget {

	protected final InkStorageBlockEntity<?> blockEntity;
	
	public StackedInkMeterWidget(int x, int y, int width, int height, InkStorageBlockEntity<?> blockEntity) {
		super(x, y, width, height, Component.empty());
		this.blockEntity = blockEntity;
	}
	
	@Override
	protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		InkStorage inkStorage = this.blockEntity.getEnergyStorage();
		long currentTotal = inkStorage.getCurrentTotal();
		
		if (currentTotal > 0) {
			long maxTotal = inkStorage.getMaxTotal();
			
			int currentHeight = getX() + getHeight();
			for (InkColor color : InkColors.all()) {
				long amount = inkStorage.getEnergy(color);
				if (amount > 0) {
					int height = Math.round(((float) amount / (float) maxTotal * getHeight()));
					if (height > 0) {
						RenderHelper.fillQuad(guiGraphics.pose(), getX(), currentHeight - height, height, getWidth(), color.getColorVec());
					}
					currentHeight -= height;
				}
			}
		}
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
	
	}
	
	public void drawMouseoverTooltip(GuiGraphics drawContext, int x, int y) {
		Minecraft client = Minecraft.getInstance();
		InkStorage inkStorage = this.blockEntity.getEnergyStorage();
		long currentTotal = inkStorage.getCurrentTotal();
		String readableCurrentTotalString = Support.getShortenedNumberString(currentTotal);
		String percent = Support.getSensiblePercentString(inkStorage.getCurrentTotal(), (inkStorage.getMaxTotal()));
		drawContext.renderTooltip(client.font, List.of(Component.translatable("spectrum.tooltip.ink_powered.percent_filled", readableCurrentTotalString, percent)),
				Optional.empty(), x, y);
	}
	
}
