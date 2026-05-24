package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.inventories.widgets.ink.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

public class TintingStationScreen extends InkTransferScreen {
	
	protected static final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/tinting_station.png");
	
	protected InkListWidget inkListWidget;
	
	public TintingStationScreen(InkTransferScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title, BACKGROUND);
	}
	
	@Override
	protected void init() {
		super.init();
		
		this.inkListWidget = new InkListWidgetWithBorderAndTitle(getGuiLeft() + 140, getGuiTop() + 40, 40, this.menu.getBlockEntity());
		inkListWidget.setPosition(getGuiLeft() - this.inkListWidget.getWidth(), getGuiTop() + 40);
		addRenderableWidget(inkListWidget);
	}
	
	@Override
	protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
		super.renderTooltip(guiGraphics, x, y);
		
		if (this.inkListWidget.isHoveredOrFocused()) {
			guiGraphics.renderTooltip(this.font, this.inkListWidget.getTooltip().toCharSequence(this.minecraft), x, y);
		}
	}
	
}