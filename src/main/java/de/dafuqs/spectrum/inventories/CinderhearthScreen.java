package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.widgets.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class CinderhearthScreen extends HandledScreen<CinderhearthScreenHandler> {
	
	protected final Identifier BACKGROUND = SpectrumCommon.locate("textures/gui/container/cinderhearth.png");
	
	protected InkMeterWidget inkMeterWidget;
	
	public CinderhearthScreen(CinderhearthScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 166;
	}
	
	@Override
	protected void init() {
		super.init();
		
		int startX = (this.width - this.backgroundWidth) / 2;
		int startY = (this.height - this.backgroundHeight) / 2;
		
		this.inkMeterWidget = new InkMeterWidget(startX + 140, startY + 34, 40, this, this.handler.getBlockEntity());
	}
	
	@Override
	protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
		int titleY = 6;
		Text title = this.title;
		
		drawContext.drawText(this.textRenderer, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawText(this.textRenderer, this.playerInventoryTitle, ColorPickerScreenHandler.PLAYER_INVENTORY_START_X, ColorPickerScreenHandler.PLAYER_INVENTORY_START_Y - 10, RenderHelper.GREEN_COLOR, false);
	}
	
	@Override
	protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
		int startX = (this.width - this.backgroundWidth) / 2;
		int startY = (this.height - this.backgroundHeight) / 2;
		
		// main background
		drawContext.drawTexture(BACKGROUND, startX, startY, 0, 0, backgroundWidth, backgroundHeight);
		
		this.inkMeterWidget.draw(drawContext);
		
		int craftingTime = this.handler.getCraftingTime();
		int craftingTimeTotal = this.handler.getCraftingTimeTotal();
		
		if (this.handler.getBlockEntity().getEnergyStorage().getEnergy(InkColors.ORANGE) > 0) {
			drawContext.drawTexture(BACKGROUND, this.x + 14, this.y + 62, 176, 14, 15, 2);
		}
		
		if (craftingTimeTotal > 0) {
			// the fire
			drawContext.drawTexture(BACKGROUND, this.x + 15, this.y + 48, 176, 0, 14, 14);
			
			// the arrow
			drawContext.drawTexture(BACKGROUND, this.x + 35, this.y + 32, 176, 16, (craftingTime * 22) / craftingTimeTotal, 16);
		}
		
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext);
		super.render(drawContext, mouseX, mouseY, delta);
		drawMouseoverTooltip(drawContext, mouseX, mouseY);
	}
	
	@Override
	protected void drawMouseoverTooltip(DrawContext drawContext, int x, int y) {
		if (this.inkMeterWidget.isMouseOver(x, y)) {
			this.inkMeterWidget.drawMouseoverTooltip(drawContext, x, y);
		} else {
			super.drawMouseoverTooltip(drawContext, x, y);
		}
	}
	
}