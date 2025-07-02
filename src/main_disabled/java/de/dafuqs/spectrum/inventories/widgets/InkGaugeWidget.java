package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class InkGaugeWidget implements Renderable, GuiEventListener, NarratableEntry {
	
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	protected boolean focused;
	protected boolean hovered;
	
	protected final Screen screen;
	protected final InkStorageBlockEntity<?> blockEntity;
	
	public InkGaugeWidget(int x, int y, int width, int height, Screen screen, InkStorageBlockEntity<?> blockEntity) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.screen = screen;
		this.blockEntity = blockEntity;
	}
	
	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= (double) this.x && mouseX < (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY < (double) (this.y + this.height);
	}
	
	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	@Override
	public boolean isFocused() {
		return focused;
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	}
	
	@Override
	public NarrationPriority narrationPriority() {
		return this.hovered ? NarrationPriority.HOVERED : NarrationPriority.NONE;
	}
	
	@Override
	public void updateNarration(NarrationElementOutput builder) {
	
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
			tooltip.add(0, Component.translatable("spectrum.tooltip.ink_powered.stored"));
		}
		drawContext.renderTooltip(client.font, tooltip, Optional.empty(), x, y);
	}
	
	public void draw(GuiGraphics drawContext) {
		long totalInk = blockEntity.getEnergyStorage().getCurrentTotal();
		
		if (totalInk > 0) {
			int centerX = x + width / 2;
			int centerY = y + width / 2;
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
						
						RenderHelper.fillTriangle(drawContext.pose(),
								centerX, centerY, // center point
								centerX + p3x, centerY + p3y, // end point
								centerX + p2x, centerY + p2y, // start point
								color.getColorVec());
						
						double middleRad = startRad + curr * Math.PI;
						int pmx = (int) (radius * Math.cos(middleRad));
						int pmy = (int) (radius * Math.sin(middleRad));
						RenderHelper.fillTriangle(drawContext.pose(),
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
}
