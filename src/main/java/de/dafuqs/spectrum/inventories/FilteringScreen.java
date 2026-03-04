package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;

public class FilteringScreen extends AbstractContainerScreen<FilteringScreenHandler> {
	
	public static final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/filter.png");
	public static final int STRIP_OFFSET = 144;
	public static final int STRIP_HEIGHT = 16;
	public static final int PLAYER_OFFSET = 55;
	public static final int PLAYER_HEIGHT = 89;
	public static final int BASE_FILTER_HEIGHT = 44;
	private final int rows;
	
	public FilteringScreen(FilteringScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.rows = handler.getRows() - 1;
		this.imageHeight = BASE_FILTER_HEIGHT + PLAYER_HEIGHT + ((int) Math.round(rows * 1.5) * STRIP_HEIGHT);
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (imageWidth - font.width(title)) / 2;
		int titleY = 6;
		Component title = this.title;
		int inventoryX = 8;
		int intInventoryY = 41 + ((int) Math.round(rows * 1.5) * STRIP_HEIGHT);
		
		drawContext.drawString(this.font, title, titleX, titleY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
		drawContext.drawString(this.font, this.playerInventoryTitle, inventoryX, intInventoryY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		drawContext.blit(BACKGROUND, x, y, 0, 0, imageWidth, BASE_FILTER_HEIGHT);
		var drawRows = (int) Math.round(rows * 1.5);
		for (int i = 0; i < drawRows; i++) {
			drawContext.blit(BACKGROUND, x, y + BASE_FILTER_HEIGHT + i * STRIP_HEIGHT, 0, STRIP_OFFSET, imageWidth, STRIP_HEIGHT);
		}
		
		drawContext.blit(BACKGROUND, x, y + BASE_FILTER_HEIGHT + drawRows * STRIP_HEIGHT, 0, PLAYER_OFFSET, imageWidth, PLAYER_HEIGHT);
		
		for (int i = 0; i < Math.min(menu.filterInventory.getContainerSize(), menu.drawnSlots); i++) {
			Slot s = menu.getSlot(i);
			drawContext.blit(BACKGROUND, x + s.x - 1, y + s.y - 1, 176, 0, 18, 18);
		}
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		renderTooltip(drawContext, mouseX, mouseY);
	}
	
}