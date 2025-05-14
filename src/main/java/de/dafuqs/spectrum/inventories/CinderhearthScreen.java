package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.cinderhearth.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.widgets.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;

public class CinderhearthScreen extends AbstractContainerScreen<CinderhearthScreenHandler> {
	
	protected final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/cinderhearth.png");
	
	protected InkMeterWidget inkMeterWidget;
	
	public CinderhearthScreen(CinderhearthScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 166;
	}
	
	@Override
	protected void init() {
		super.init();
		
		int startX = (this.width - this.imageWidth) / 2;
		int startY = (this.height - this.imageHeight) / 2;
		
		this.inkMeterWidget = new InkMeterWidget(startX + 140, startY + 34, 40, this, this.menu.getBlockEntity());
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (imageWidth - font.width(title)) / 2;
		int titleY = 6;
		Component title = this.title;
		
		drawContext.drawString(this.font, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawString(this.font, this.playerInventoryTitle, ColorPickerScreenHandler.PLAYER_INVENTORY_START_X, ColorPickerScreenHandler.PLAYER_INVENTORY_START_Y - 10, RenderHelper.GREEN_COLOR, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		int startX = (this.width - this.imageWidth) / 2;
		int startY = (this.height - this.imageHeight) / 2;
		
		// main background
		drawContext.blit(BACKGROUND, startX, startY, 0, 0, imageWidth, imageHeight);
		
		this.inkMeterWidget.draw(drawContext, CinderhearthBlockEntity.USED_INK_COLORS);
		
		int craftingTime = this.menu.getCraftingTime();
		int craftingTimeTotal = this.menu.getCraftingTimeTotal();
		
		if (this.menu.getBlockEntity().getEnergyStorage().getEnergy(InkColors.ORANGE) > 0) {
			drawContext.blit(BACKGROUND, this.leftPos + 14, this.topPos + 62, 176, 14, 15, 2);
		}
		
		if (craftingTimeTotal > 0) {
			// the fire
			drawContext.blit(BACKGROUND, this.leftPos + 15, this.topPos + 48, 176, 0, 14, 14);
			
			// the arrow
			drawContext.blit(BACKGROUND, this.leftPos + 35, this.topPos + 32, 176, 16, (craftingTime * 22) / craftingTimeTotal, 16);
		}
		
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		renderTooltip(drawContext, mouseX, mouseY);
	}
	
	@Override
	protected void renderTooltip(GuiGraphics drawContext, int x, int y) {
		if (this.inkMeterWidget.isMouseOver(x, y)) {
			this.inkMeterWidget.drawMouseoverTooltip(drawContext, x, y);
		} else {
			super.renderTooltip(drawContext, x, y);
		}
	}
	
}
