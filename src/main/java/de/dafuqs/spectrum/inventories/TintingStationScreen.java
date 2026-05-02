package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.inventories.widgets.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;

public class TintingStationScreen extends InkTransferScreen {
	
	protected final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/color_picker.png");
	
	protected InkMeterWidget inkMeterWidget;
	
	public TintingStationScreen(InkTransferScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
	}
	
	@Override
	protected void init() {
		super.init();
		
		int startX = (this.width - this.imageWidth) / 2;
		int startY = (this.height - this.imageHeight) / 2;
		
		this.inkGaugeWidget = new InkGaugeWidget(startX + 54, startY + 21, 42, 42, this.menu.getBlockEntity());
		addWidget(inkGaugeWidget);
		this.inkMeterWidget = new InkMeterWidget(startX + 140, startY + 34, 40, (InkStorageBlockEntity<IndividualCappedInkStorage>) this.menu.getBlockEntity(), InkColors.all());
		addWidget(inkMeterWidget);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		super.renderBg(drawContext, delta, mouseX, mouseY);
		int startX = (this.width - this.imageWidth) / 2;
		int startY = (this.height - this.imageHeight) / 2;
		
		// main background
		drawContext.blit(BACKGROUND, startX, startY, 0, 0, imageWidth, imageHeight);
		// this.inkMeterWidget.render(drawContext, ((InkStorageBlockEntity<IndividualCappedInkStorage>) this.menu.getBlockEntity()).getEnergyStorage().getEnergy().keySet());
		
		super.renderBg(drawContext, delta, mouseX, mouseY);
	}
	
}