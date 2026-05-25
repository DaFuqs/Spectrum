package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.inventories.widgets.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import net.minecraft.client.gui.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.neoforge.network.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public abstract class InkTransferScreen<T extends InkStorageWithColorSelectionScreenHandler> extends InkStorageScreen<T> implements Consumer<Optional<Holder<InkColor>>> {
	
	protected ColorSelectionWidget colorSelectionWidget;
	
	public InkTransferScreen(T handler, Inventory playerInventory, Component title, ResourceLocation background) {
		super(handler, playerInventory, title, background);
	}
	
	@Override
	protected void init() {
		super.init();
		this.colorSelectionWidget = new ColorSelectionWidget(getGuiLeft() + 113, getGuiTop() + 55, getGuiLeft() + 139, getGuiTop() + 25, this.menu.getBlockEntity());
		this.colorSelectionWidget.setChangedListener(this);
		addRenderableWidget(this.colorSelectionWidget);
	}
	
	@Override
	protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
		super.renderTooltip(guiGraphics, x, y);
		
		if (this.colorSelectionWidget.isMouseOver(x, y)) {
			this.colorSelectionWidget.drawMouseoverTooltip(guiGraphics, x, y);
		}
	}
	
	@Override
	public void accept(Optional<Holder<InkColor>> inkColor) {
		BaseInkTransferBlockEntity<?> colorPicker = this.menu.getBlockEntity();
		colorPicker.setSelectedColor(inkColor);
		PacketDistributor.sendToServer(new InkColorSelectedC2SPayload(inkColor));
	}
	
}