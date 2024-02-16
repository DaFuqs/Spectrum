package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.narration.*;
import net.minecraft.text.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.Support.*;

@Environment(EnvType.CLIENT)
public class InkGaugeWidget implements Drawable, Element, Selectable {
	
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
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	}
	
	@Override
	public SelectionType getType() {
		return this.hovered ? SelectionType.HOVERED : SelectionType.NONE;
	}
	
	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
	
	}
	
	public void drawMouseoverTooltip(DrawContext drawContext, int x, int y) {
		MinecraftClient client = MinecraftClient.getInstance();
		List<Text> tooltip = new ArrayList<>();
		for (InkColor color : InkColor.all()) {
			long amount = blockEntity.getEnergyStorage().getEnergy(color);
			if (amount > 0) {
				tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.bullet." + color.toString().toLowerCase(Locale.ROOT), getShortenedNumberString(amount)));
			}
		}
		if (tooltip.size() == 0) {
			tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.empty"));
		} else {
			tooltip.add(0, Text.translatable("spectrum.tooltip.ink_powered.stored"));
		}
		drawContext.drawTooltip(client.textRenderer, tooltip, Optional.empty(), x, y);
	}
	
	public void draw(DrawContext drawContext) {
		long totalInk = blockEntity.getEnergyStorage().getCurrentTotal();
		
		if (totalInk > 0) {
			int centerX = x + width / 2;
			int centerY = y + width / 2;
			int radius = 22;
			
			double startRad = -0.5 * Math.PI;
			for (InkColor color : InkColor.all()) {
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
						
						RenderHelper.fillTriangle(drawContext.getMatrices(),
								centerX, centerY, // center point
								centerX + p3x, centerY + p3y, // end point
								centerX + p2x, centerY + p2y, // start point
								color.getColor());
						
						double middleRad = startRad + curr * Math.PI;
						int pmx = (int) (radius * Math.cos(middleRad));
						int pmy = (int) (radius * Math.sin(middleRad));
						RenderHelper.fillTriangle(drawContext.getMatrices(),
								centerX + p3x, centerY + p3y,
								centerX + pmx, centerY + pmy,
								centerX + p2x, centerY + p2y,
								color.getColor());
						
						startRad = endRad;
					}
				}
			}
		}
	}
}
