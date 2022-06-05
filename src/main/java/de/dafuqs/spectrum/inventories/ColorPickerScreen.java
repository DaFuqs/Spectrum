package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.inventories.widgets.ColorSelectionWidget;
import de.dafuqs.spectrum.inventories.widgets.InkGaugeWidget;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ColorPickerScreen extends HandledScreen<ColorPickerScreenHandler> implements ScreenHandlerListener {
	
	protected final Identifier BACKGROUND = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/color_picker.png");
	protected ColorSelectionWidget colorSelectionWidget;
	protected InkGaugeWidget inkGaugeWidget;
	
	public ColorPickerScreen(ColorPickerScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 166;
	}
	
	@Override
	protected void init() {
		super.init();
		int centerX = (this.width - this.backgroundWidth) / 2;
		int centerY = (this.height - this.backgroundHeight) / 2;
		
		this.colorSelectionWidget = new ColorSelectionWidget(centerX + 62, centerY + 24, 98, 12, this.handler.getBlockEntity().getSelectedColor());
		this.inkGaugeWidget = new InkGaugeWidget(centerX + 62, centerY + 24, 98, 12);
		handler.addListener(this);
	}
	
	@Override
	public void removed() {
		super.removed();
		handler.removeListener(this);
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
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		super.drawMouseoverTooltip(matrices, x, y);
		
		
	}
	
	@Override
	public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
	
	}
	
	@Override
	public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
	
	}
	
}