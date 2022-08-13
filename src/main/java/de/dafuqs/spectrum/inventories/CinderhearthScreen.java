package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.inventories.widgets.InkMeterWidget;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CinderhearthScreen extends HandledScreen<CinderhearthScreenHandler> {
	
	protected final Identifier BACKGROUND = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/cinderhearth.png");
	
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
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
		int titleY = 6;
		Text title = this.title;
		
		this.textRenderer.draw(matrices, title, titleX, titleY, 3289650);
		this.textRenderer.draw(matrices, this.playerInventoryTitle, ColorPickerScreenHandler.PLAYER_INVENTORY_START_X, ColorPickerScreenHandler.PLAYER_INVENTORY_START_Y - 10, 3289650);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		int startX = (this.width - this.backgroundWidth) / 2;
		int startY = (this.height - this.backgroundHeight) / 2;
		
		// main background
        drawTexture(matrices, startX, startY, 0, 0, backgroundWidth, backgroundHeight);

		this.inkMeterWidget.draw(matrices);
		
		int craftingTime = this.handler.getCraftingTime();
		int craftingTimeTotal = this.handler.getCraftingTimeTotal();
		
		if(this.handler.getBlockEntity().getEnergyStorage().getEnergy(InkColors.ORANGE) > 0) {
			this.drawTexture(matrices, this.x + 14, this.y + 62, 176, 14, 15, 2);
		}
		
		if (craftingTimeTotal > 0) {
			// the fire
			this.drawTexture(matrices, this.x + 15, this.y + 48, 176, 0, 14, 14);
			
			// the arrow
			this.drawTexture(matrices, this.x + 35, this.y + 32, 176, 16, (craftingTime * 22) / craftingTimeTotal, 16);
		}
		
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if(this.inkMeterWidget.isMouseOver(x, y)) {
			this.inkMeterWidget.drawMouseoverTooltip(matrices, x, y);
		} else {
			super.drawMouseoverTooltip(matrices, x, y);
		}
	}
	
}