package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.inventories.widgets.ink.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

public class TintingStationScreen extends InkTransferScreen {
	
	protected final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/color_picker.png");
	
	protected InkListWidget inkListWidget;
	
	public TintingStationScreen(InkTransferScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
	}
	
	@Override
	protected void init() {
		super.init();
		
		this.inkListWidget = new InkListWidget(getGuiLeft() + 140, getGuiTop() + 34, 40, (InkStorageBlockEntity<IndividualCappedInkStorage>) this.menu.getBlockEntity(), InkColors.all());
		addRenderableWidget(inkListWidget);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		super.renderBg(drawContext, delta, mouseX, mouseY);
		drawContext.blit(BACKGROUND, getGuiLeft(), getGuiTop(), 0, 0, imageWidth, imageHeight);
		super.renderBg(drawContext, delta, mouseX, mouseY);
	}
	
	@Override
	protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
		super.renderTooltip(guiGraphics, x, y);
		
		if (this.inkListWidget.isHoveredOrFocused()) {
			guiGraphics.renderTooltip(this.font, this.inkListWidget.getTooltip().toCharSequence(Minecraft.getInstance()), x, y);
		}
	}
	
}