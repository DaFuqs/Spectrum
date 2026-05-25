package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.widgets.ink.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;

public class CinderhearthScreen extends AbstractContainerScreen<CinderhearthScreenHandler> {
	
	protected final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/cinderhearth.png");
	
	protected InkListWidget inkListWidget;
	
	public CinderhearthScreen(CinderhearthScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 166;
	}
	
	@Override
	protected void init() {
		super.init();
		this.inkListWidget = new InkListWidget(getGuiLeft() + 140, getGuiTop() + 34, 40, this.menu.getBlockEntity());
		addRenderableWidget(inkListWidget);
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (imageWidth - font.width(title)) / 2;
		int titleY = 6;
		Component title = this.title;
		
		drawContext.drawString(this.font, title, titleX, titleY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
		drawContext.drawString(this.font, this.playerInventoryTitle, InkStorageWithColorSelectionScreenHandler.PLAYER_INVENTORY_START_X, InkStorageWithColorSelectionScreenHandler.PLAYER_INVENTORY_START_Y - 10, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		// main background
		guiGraphics.blit(BACKGROUND, getGuiLeft(), getGuiTop(), 0, 0, imageWidth, imageHeight);
		
		int craftingTime = this.menu.getCraftingTime();
		int craftingTimeTotal = this.menu.getCraftingTimeTotal();
		
		if (this.menu.getBlockEntity().getInkStorage().getEnergy(InkColors.ORANGE) > 0) {
			guiGraphics.blit(BACKGROUND, this.leftPos + 14, this.topPos + 62, 176, 14, 15, 2);
		}
		
		if (craftingTimeTotal > 0) {
			// the fire
			guiGraphics.blit(BACKGROUND, this.leftPos + 15, this.topPos + 48, 176, 0, 14, 14);
			
			// the arrow
			guiGraphics.blit(BACKGROUND, this.leftPos + 35, this.topPos + 32, 176, 16, (craftingTime * 22) / craftingTimeTotal, 16);
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
		super.renderTooltip(drawContext, x, y);
		
		if (this.inkListWidget.isHoveredOrFocused()) {
			drawContext.renderTooltip(this.font, this.inkListWidget.getTooltip().toCharSequence(this.minecraft), x, y);
		}
	}
	
}
