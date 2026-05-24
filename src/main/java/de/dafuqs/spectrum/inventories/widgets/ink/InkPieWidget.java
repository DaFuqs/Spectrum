package de.dafuqs.spectrum.inventories.widgets.ink;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.*;

import javax.annotation.Nullable;


public class InkPieWidget extends AbstractWidget {
	
	protected final InkStorageBlockEntity<?> blockEntity;
	
	public InkPieWidget(int x, int y, int width, int height, InkStorageBlockEntity<?> blockEntity) {
		super(x, y, width, height, Component.empty());
		this.blockEntity = blockEntity;
	}
	
	@Override
	protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		long totalInk = blockEntity.getInkStorage().getCurrentTotal();
		
		if (totalInk > 0) {
			int centerX = getX() + width / 2;
			int centerY = getY() + width / 2;
			int radius = 22;
			
			double startRad = -0.5 * Math.PI;
			for (InkColor color : InkColors.all()) {
				long currentInk = blockEntity.getInkStorage().getEnergy(color);
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
	
	@Nullable
	public Tooltip getTooltip() {
		return blockEntity.getInkStorage().getWidgetTooltip();
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
	
	}
	
}
