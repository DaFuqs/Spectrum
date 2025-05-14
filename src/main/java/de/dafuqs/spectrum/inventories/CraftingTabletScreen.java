package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;

public class CraftingTabletScreen extends AbstractContainerScreen<CraftingTabletScreenHandler> {
	
	public static final ResourceLocation BACKGROUND1 = SpectrumCommon.locate("textures/gui/container/crafting_tablet1.png");
	public static final ResourceLocation BACKGROUND2 = SpectrumCommon.locate("textures/gui/container/crafting_tablet2.png");
	public static final ResourceLocation BACKGROUND3 = SpectrumCommon.locate("textures/gui/container/crafting_tablet3.png");
	public static final ResourceLocation BACKGROUND4 = SpectrumCommon.locate("textures/gui/container/crafting_tablet4.png");
	
	protected ResourceLocation backgroundTexture;
	
	public CraftingTabletScreen(CraftingTabletScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 194;
		
		backgroundTexture = BACKGROUND1;
		if (handler.getTier().isPresent()) {
			switch (handler.getTier().get()) {
				case COMPLEX -> backgroundTexture = BACKGROUND4;
				case ADVANCED -> backgroundTexture = BACKGROUND3;
				case SIMPLE -> backgroundTexture = BACKGROUND2;
				default -> {}
			}
		}
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (imageWidth - font.width(title)) / 2;
		int titleY = 7;
		Component title = this.title;
		int inventoryX = 8;
		int intInventoryY = 100;
		
		drawContext.drawString(this.font, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawString(this.font, this.playerInventoryTitle, inventoryX, intInventoryY, RenderHelper.GREEN_COLOR, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		drawContext.blit(backgroundTexture, x, y, 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		renderTooltip(drawContext, mouseX, mouseY);
	}
	
}