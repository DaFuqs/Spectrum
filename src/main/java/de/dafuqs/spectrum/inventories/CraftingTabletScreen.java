package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
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
	
	protected Identifier backgroundTexture;
	
	public CraftingTabletScreen(CraftingTabletScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 194;
		
		backgroundTexture = BACKGROUND1;
		if (handler.getTier().isPresent()) {
			switch (handler.getTier().get()) {
				case COMPLEX -> backgroundTexture = BACKGROUND4;
				case ADVANCED -> backgroundTexture = BACKGROUND3;
				case SIMPLE -> backgroundTexture = BACKGROUND2;
			}
		}
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
		int titleY = 7;
		Text title = this.title;
		int inventoryX = 8;
		int intInventoryY = 100;
		
		this.textRenderer.draw(matrices, title, titleX, titleY, 3289650);
		this.textRenderer.draw(matrices, this.playerInventoryTitle, inventoryX, intInventoryY, 3289650);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, backgroundTexture);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
}