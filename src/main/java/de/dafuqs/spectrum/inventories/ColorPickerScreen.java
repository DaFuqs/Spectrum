package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.inventories.widgets.ink.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

public class ColorPickerScreen extends InkTransferScreen {
	
	protected final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/color_picker.png");
	
	protected StackedInkBarWidget stackedInkBarWidget;
	protected InkPieWidget inkPieWidget;
	
	public ColorPickerScreen(InkTransferScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 166;
	}
	
	
	@Override
	protected void init() {
		super.init();
		
		this.inkPieWidget = new InkPieWidget(getGuiLeft() + 54, getGuiTop() + 21, 42, 42, this.menu.getBlockEntity());
		addRenderableWidget(this.inkPieWidget);
		this.stackedInkBarWidget = new StackedInkBarWidget(getGuiLeft() + 100, getGuiTop() + 21, 4, 40, this.menu.getBlockEntity());
		addRenderableWidget(stackedInkBarWidget);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float partialTick, int mouseX, int mouseY) {
		drawContext.blit(BACKGROUND, getGuiLeft(), getGuiTop(), 0, 0, imageWidth, imageHeight);
		super.renderBg(drawContext, partialTick, mouseX, mouseY);
	}
	
	@Override
	protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
		super.renderTooltip(guiGraphics, x, y);
		
		if (this.inkPieWidget.isHoveredOrFocused()) {
			guiGraphics.renderTooltip(this.font, this.inkPieWidget.getTooltip().toCharSequence(Minecraft.getInstance()), x, y);
		}
		if (this.stackedInkBarWidget.isHoveredOrFocused()) {
			guiGraphics.renderTooltip(this.font, this.stackedInkBarWidget.getTooltip().toCharSequence(Minecraft.getInstance()), x, y);
		}
	}
	
}