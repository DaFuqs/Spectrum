package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class CraftingTabletScreen extends HandledScreen<CraftingTabletScreenHandler> {
	
	public static final Identifier BACKGROUND1 = SpectrumCommon.locate("textures/gui/container/crafting_tablet1.png");
	public static final Identifier BACKGROUND2 = SpectrumCommon.locate("textures/gui/container/crafting_tablet2.png");
	public static final Identifier BACKGROUND3 = SpectrumCommon.locate("textures/gui/container/crafting_tablet3.png");
	public static final Identifier BACKGROUND4 = SpectrumCommon.locate("textures/gui/container/crafting_tablet4.png");
	
	public Identifier BACKGROUND;
	
	public CraftingTabletScreen(CraftingTabletScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 194;
		
		if (handler.getTier().isPresent()) {
			switch (handler.getTier().get()) {
				case COMPLEX -> BACKGROUND = BACKGROUND4;
				case ADVANCED -> BACKGROUND = BACKGROUND3;
				case SIMPLE -> BACKGROUND = BACKGROUND2;
				case BASIC -> BACKGROUND = BACKGROUND1;
			}
		}
	}
	
	@Override
	protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
		int titleY = 7;
		Text title = this.title;
		int inventoryX = 8;
		int intInventoryY = 100;

		drawContext.drawText(this.textRenderer, title, titleX, titleY, RenderHelper.GREEN_COLOR,false);
		drawContext.drawText(this.textRenderer, this.playerInventoryTitle, inventoryX, intInventoryY, RenderHelper.GREEN_COLOR,false);
	}
	
	@Override
	protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawContext.drawTexture(BACKGROUND, x, y, 0, 0, backgroundWidth, backgroundHeight);
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext);
		super.render(drawContext, mouseX, mouseY, delta);
		drawMouseoverTooltip(drawContext, mouseX, mouseY);
	}
	
}