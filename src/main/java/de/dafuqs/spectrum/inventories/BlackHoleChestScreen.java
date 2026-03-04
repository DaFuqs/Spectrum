package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;

public class BlackHoleChestScreen extends AbstractContainerScreen<BlackHoleChestScreenHandler> {
	
	public static final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/black_hole_chest.png");
	
	public BlackHoleChestScreen(BlackHoleChestScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 193;
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (imageWidth - font.width(title)) / 2;
		int titleY = 6;
		Component title = this.title;
		int inventoryX = 8;
		int intInventoryY = 102;
		
		drawContext.drawString(this.font, title, titleX, titleY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
		drawContext.drawString(this.font, this.playerInventoryTitle, inventoryX, intInventoryY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		drawContext.blit(BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		renderTooltip(drawContext, mouseX, mouseY);
	}
	
}