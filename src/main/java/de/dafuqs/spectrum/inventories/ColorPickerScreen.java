package de.dafuqs.spectrum.inventories;

import com.google.common.primitives.UnsignedInteger;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.inventories.widgets.ColorSelectionWidget;
import de.dafuqs.spectrum.inventories.widgets.InkGaugeWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.util.List;

public class ColorPickerScreen extends HandledScreen<ColorPickerScreenHandler> implements ScreenHandlerListener {
    BufferBuilder builder = Tessellator.getInstance().getBuffer();

    Window window = MinecraftClient.getInstance().getWindow();
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
        //this.textRenderer.draw(matrices, String.valueOf(inkGaugeWidget.x), titleX, titleY, 3289650);
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
        fillTri(matrices.peek().getPositionMatrix(), window.getScaledWidth()-startX, 0, 150, 125,startX + (inkGaugeWidget.x + (inkGaugeWidget.width/2)),startY + (inkGaugeWidget.height/2), List.of(1F, 0.0F, 1.0F, 1.0F));
        drawTexture(matrices, startX+54, startY+20, 176 ,0 ,42 , 42);

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

    public void fillTri(Matrix4f matrix, Integer p1x, Integer p1y, Integer p2x, Integer p2y, Integer p3x, Integer p3y, List<Float> color){
        Float alpha = color.get(0);
        Float red = color.get(1);
        Float green = color.get(2);
        Float blue = color.get(3);
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        builder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
        builder.vertex(matrix, p1x, p1y, 0F).color(red, green, blue, alpha).next();
        builder.vertex(matrix, p2x, p2y, 0F).color(red, green, blue, alpha).next();
        builder.vertex(matrix, p3x, p3y, 0F).color(red, green, blue, alpha).next();
        builder.end();
        BufferRenderer.draw(builder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private Integer getAbsolutePos(Integer relativeX, Integer relativeY, Integer startX, Integer startY){


        return null;
    }
}