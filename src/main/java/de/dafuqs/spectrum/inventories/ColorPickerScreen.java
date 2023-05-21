package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.inventories.widgets.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.function.*;

public class ColorPickerScreen extends HandledScreen<ColorPickerScreenHandler> implements Consumer<InkColor> {
	
	protected final Identifier BACKGROUND = SpectrumCommon.locate("textures/gui/container/color_picker.png");
	protected ColorSelectionWidget colorSelectionWidget;
	protected InkGaugeWidget inkGaugeWidget;
	protected StackedInkMeterWidget inkMeterWidget;
	
	public ColorPickerScreen(ColorPickerScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 166;
	}
	
	@Override
	protected void init() {
		super.init();
		
		int startX = (this.width - this.backgroundWidth) / 2;
		int startY = (this.height - this.backgroundHeight) / 2;
		
		this.colorSelectionWidget = new ColorSelectionWidget(startX + 113, startY + 55, startX + 139, startY + 25, this, this.handler.getBlockEntity());
		this.inkGaugeWidget = new InkGaugeWidget(startX + 54, startY + 21, 42, 42, this, this.handler.getBlockEntity());
		this.inkMeterWidget = new StackedInkMeterWidget(startX + 100, startY + 21, 4, 40, this, this.handler.getBlockEntity());
		
		this.colorSelectionWidget.setChangedListener(this);
		
		addSelectableChild(this.colorSelectionWidget);
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
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		int startX = (this.width - this.backgroundWidth) / 2;
		int startY = (this.height - this.backgroundHeight) / 2;
		
		// main background
		drawTexture(matrices, startX, startY, 0, 0, backgroundWidth, backgroundHeight);
		
		this.inkGaugeWidget.draw(matrices);
		this.inkMeterWidget.draw(matrices);
		this.colorSelectionWidget.draw(matrices);
		
		// gauge blanket
		drawTexture(matrices, startX + 52, startY + 18, 176, 0, 46, 46);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (this.inkGaugeWidget.isMouseOver(x, y)) {
			this.inkGaugeWidget.drawMouseoverTooltip(matrices, x, y);
		} else if (this.inkMeterWidget.isMouseOver(x, y)) {
			this.inkMeterWidget.drawMouseoverTooltip(matrices, x, y);
		} else if (this.colorSelectionWidget.isMouseOver(x, y)) {
			this.colorSelectionWidget.drawMouseoverTooltip(matrices, x, y);
		} else {
			super.drawMouseoverTooltip(matrices, x, y);
		}
	}
	
	@Override
	public void accept(InkColor inkColor) {
		ColorPickerBlockEntity colorPicker = this.handler.getBlockEntity();
		colorPicker.setSelectedColor(inkColor);
		SpectrumC2SPacketSender.sendInkColorSelectedInGUI(inkColor);
	}
	
}