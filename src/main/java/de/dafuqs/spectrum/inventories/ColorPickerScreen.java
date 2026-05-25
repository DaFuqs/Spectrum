package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.inventories.widgets.*;
import de.dafuqs.spectrum.inventories.widgets.ink.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

public class ColorPickerScreen extends InkStorageScreen<ColorPickerScreenHandler> {
	
	protected static final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/color_picker.png");
	
	protected StackedInkBarWidget stackedInkBarWidget;
	protected InkPieWidget inkPieWidget;
	
	public ColorPickerScreen(ColorPickerScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title, BACKGROUND);
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
	protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
		super.renderTooltip(guiGraphics, x, y);
		
		if (this.inkPieWidget.isHoveredOrFocused()) {
			guiGraphics.renderTooltip(this.font, this.inkPieWidget.getTooltip().toCharSequence(this.minecraft), x, y);
		}
		if (this.stackedInkBarWidget.isHoveredOrFocused()) {
			guiGraphics.renderTooltip(this.font, this.stackedInkBarWidget.getTooltip().toCharSequence(this.minecraft), x, y);
		}
	}
	
}