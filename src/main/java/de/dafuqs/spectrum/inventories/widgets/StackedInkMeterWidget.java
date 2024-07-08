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

@Environment(EnvType.CLIENT)
public class StackedInkMeterWidget implements Drawable, Element, Selectable {
	
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	protected boolean hovered;
	protected boolean focused;
	
	protected final Screen screen;
	protected final InkStorageBlockEntity<?> blockEntity;
	
	public StackedInkMeterWidget(int x, int y, int width, int height, Screen screen, InkStorageBlockEntity<?> blockEntity) {
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
		InkStorage inkStorage = this.blockEntity.getEnergyStorage();
		long currentTotal = inkStorage.getCurrentTotal();
		String readableCurrentTotalString = Support.getShortenedNumberString(currentTotal);
		String percent = Support.getSensiblePercent(inkStorage.getCurrentTotal(), (inkStorage.getMaxTotal()));
		drawContext.drawTooltip(client.textRenderer,List.of(Text.translatable("spectrum.tooltip.ink_powered.percent_filled", readableCurrentTotalString, percent)),
			Optional.empty(), x, y);
	}
	
	public void draw(DrawContext drawContext) {
		InkStorage inkStorage = this.blockEntity.getEnergyStorage();
		long currentTotal = inkStorage.getCurrentTotal();
		
		if (currentTotal > 0) {
			long maxTotal = inkStorage.getMaxTotal();
			
			int currentHeight = this.y + this.height;
			for (InkColor color : InkColor.all()) {
				long amount = inkStorage.getEnergy(color);
				if (amount > 0) {
					int height = Math.round(((float) amount / (float) maxTotal * this.height));
					if (height > 0) {
						RenderHelper.fillQuad(drawContext.getMatrices(), this.x, currentHeight - height, height, this.width, color.getColor());
					}
					currentHeight -= height;
				}
			}
		}
	}
	
}
