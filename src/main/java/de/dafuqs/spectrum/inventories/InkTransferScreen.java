package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.energy.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.widgets.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.neoforge.network.*;

import java.util.*;
import java.util.function.*;

public class InkTransferScreen extends AbstractContainerScreen<InkTransferScreenHandler> implements Consumer<Optional<Holder<InkColor>>> {
	
	protected final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/color_picker.png");
	
	protected ColorSelectionWidget colorSelectionWidget;
	protected AbstractWidget inkMeterWidget;
	protected InkGaugeWidget inkGaugeWidget;
	
	public InkTransferScreen(InkTransferScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 166;
	}
	
	@Override
	protected void init() {
		super.init();
		
		int startX = (this.width - this.imageWidth) / 2;
		int startY = (this.height - this.imageHeight) / 2;
		
		this.colorSelectionWidget = new ColorSelectionWidget(startX + 113, startY + 55, startX + 139, startY + 25, this.menu.getBlockEntity());
		this.colorSelectionWidget.setChangedListener(this);
		addWidget(this.colorSelectionWidget);
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		int titleX = (imageWidth - font.width(title)) / 2;
		int titleY = 6;
		Component title = this.title;
		
		drawContext.drawString(this.font, title.getVisualOrderText(), titleX, titleY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
		drawContext.drawString(this.font, this.playerInventoryTitle, InkTransferScreenHandler.PLAYER_INVENTORY_START_X, InkTransferScreenHandler.PLAYER_INVENTORY_START_Y - 10, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		int startX = (this.width - this.imageWidth) / 2;
		int startY = (this.height - this.imageHeight) / 2;
		
		// main background
		drawContext.blit(BACKGROUND, startX, startY, 0, 0, imageWidth, imageHeight);
		
		this.inkGaugeWidget.render(drawContext, mouseX, mouseY, delta);
		this.colorSelectionWidget.render(drawContext, mouseX, mouseY, delta);
		
		// gauge blanket
		drawContext.blit(BACKGROUND, startX + 52, startY + 18, 176, 0, 46, 46);
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		renderTooltip(drawContext, mouseX, mouseY);
	}
	
	@Override
	protected void renderTooltip(GuiGraphics drawContext, int x, int y) {
		if (this.inkGaugeWidget.isHoveredOrFocused()) {
			this.inkGaugeWidget.drawMouseoverTooltip(drawContext, x, y);
		//} else if (this.inkMeterWidget.isHoveredOrFocused()) { // TODO
		//	this.inkMeterWidget.dra(drawContext, x, y);
		} else if (this.colorSelectionWidget.isHoveredOrFocused()) {
			this.colorSelectionWidget.drawMouseoverTooltip(drawContext, x, y);
		} else {
			super.renderTooltip(drawContext, x, y);
		}
	}
	
	@Override
	public void accept(Optional<Holder<InkColor>> inkColor) {
		BaseInkTransferBlockEntity<?> colorPicker = this.menu.getBlockEntity();
		colorPicker.setSelectedColor(inkColor);
		PacketDistributor.sendToServer(new InkColorSelectedC2SPayload(inkColor));
	}
	
}