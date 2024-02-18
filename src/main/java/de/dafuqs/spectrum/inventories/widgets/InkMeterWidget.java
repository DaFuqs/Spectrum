package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.narration.*;
import net.minecraft.text.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class InkMeterWidget implements Drawable, Element, Selectable {
	
	public static final int WIDTH_PER_COLOR = 4;
	public static final int SPACE_BETWEEN_COLORS = 2;
	
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	protected boolean hovered;
	protected boolean focused;
	
	protected final Screen screen;
	protected final InkStorageBlockEntity<IndividualCappedInkStorage> inkStorageBlockEntity;
	
	public InkMeterWidget(int x, int y, int height, Screen screen, InkStorageBlockEntity<IndividualCappedInkStorage> inkStorageBlockEntity) {
		this.x = x;
		this.y = y;
		this.width = inkStorageBlockEntity.getEnergyStorage().getSupportedColors().size() * (WIDTH_PER_COLOR + SPACE_BETWEEN_COLORS) - SPACE_BETWEEN_COLORS;
		this.height = height;
		
		this.screen = screen;
		this.inkStorageBlockEntity = inkStorageBlockEntity;
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
	public SelectionType getType() {
		return this.hovered ? SelectionType.HOVERED : SelectionType.NONE;
	}
	
	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
	
	}
	
	public void drawMouseoverTooltip(DrawContext drawContext, int x, int y) {
		MinecraftClient client = MinecraftClient.getInstance();
		List<Text> tooltip = new ArrayList<>();
		inkStorageBlockEntity.getEnergyStorage().addTooltip(tooltip, false);
		drawContext.drawTooltip(client.textRenderer, tooltip, Optional.empty(), x, y);
	}
	
	public void draw(DrawContext drawContext) {
		int startHeight = this.y + this.height;
		int currentXOffset = 0;

		IndividualCappedInkStorage inkStorage = inkStorageBlockEntity.getEnergyStorage();
		long total = inkStorage.getMaxPerColor();
		for (InkColor inkColor : inkStorage.getSupportedColors()) {
			long amount = inkStorage.getEnergy(inkColor);
			if (amount > 0) {
				int height = Math.max(1, Math.round(((float) amount / ((float) total / this.height))));
				RenderHelper.fillQuad(drawContext.getMatrices(), this.x + currentXOffset, startHeight - height, height, WIDTH_PER_COLOR, inkColor.getColor());
			}
			currentXOffset = currentXOffset + WIDTH_PER_COLOR + SPACE_BETWEEN_COLORS;
		}
		
		
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;


	}
}
