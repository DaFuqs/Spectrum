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


public class InkGaugeWidget extends AbstractWidget {
	
	protected final InkStorageBlockEntity<?> blockEntity;
	
	public InkGaugeWidget(int x, int y, int width, int height, InkStorageBlockEntity<?> blockEntity) {
		super(x, y, width, height, Component.empty());
		this.blockEntity = blockEntity;
	}
	
	@Override
	protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
		long totalInk = blockEntity.getEnergyStorage().getCurrentTotal();
		
		if (totalInk > 0) {
			int centerX = getX() + width / 2;
			int centerY = getY() + width / 2;
			int radius = 22;
			
			double startRad = -0.5 * Math.PI;
			for (InkColor color : InkColors.all()) {
				long currentInk = blockEntity.getEnergyStorage().getEnergy(color);
				if (currentInk > 0) {
					double thisPart = ((double) currentInk / (double) totalInk);
					while (thisPart > 0) {
						double curr = Math.min(0.20, thisPart);
						thisPart -= curr;
						
						double endRad = startRad + curr * 2 * Math.PI;
						
						int p2x = (int) (radius * Math.cos(startRad));
						int p2y = (int) (radius * Math.sin(startRad));
						int p3x = (int) (radius * Math.cos(endRad));
						int p3y = (int) (radius * Math.sin(endRad));
						
						RenderHelper.fillTriangle(guiGraphics.pose(),
								centerX, centerY, // center point
								centerX + p3x, centerY + p3y, // end point
								centerX + p2x, centerY + p2y, // start point
								color.getColorVec());
						
						double middleRad = startRad + curr * Math.PI;
						int pmx = (int) (radius * Math.cos(middleRad));
						int pmy = (int) (radius * Math.sin(middleRad));
						RenderHelper.fillTriangle(guiGraphics.pose(),
								centerX + p3x, centerY + p3y,
								centerX + pmx, centerY + pmy,
								centerX + p2x, centerY + p2y,
								color.getColorVec());
						
						startRad = endRad;
					}
				}
			}
		}
	}
	
	public void drawMouseoverTooltip(GuiGraphics drawContext, int x, int y) {
		Minecraft client = Minecraft.getInstance();
		List<Component> tooltip = new ArrayList<>();
		for (InkColor color : InkColors.all()) {
			long amount = blockEntity.getEnergyStorage().getEnergy(color);
			if (amount > 0) {
				InkStorage.addInkStoreBulletTooltip(tooltip, color, amount);
			}
		}
		if (tooltip.isEmpty()) {
			tooltip.add(Component.translatable("spectrum.tooltip.ink_powered.empty"));
		} else {
			tooltip.addFirst(Component.translatable("spectrum.tooltip.ink_powered.stored"));
		}
		drawContext.renderTooltip(client.font, tooltip, Optional.empty(), x, y);
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
	
	}
	
}
