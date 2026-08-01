package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.widgets.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.neoforge.network.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class BaseInkScreen<T extends BaseInkScreenHandler> extends AbstractContainerScreen<T> implements Consumer<Optional<Holder<InkColor>>> {
	
	protected static final ResourceLocation DEFAULT_BACKGROUND = SpectrumCommon.locate("textures/gui/container/color_picker.png");
	
	protected ColorSelectionWidget colorSelectionWidget;
	protected final ResourceLocation background;
	
	public BaseInkScreen(T handler, Inventory playerInventory, Component title) {
		this(handler, playerInventory, title, DEFAULT_BACKGROUND);
	}
	
	public BaseInkScreen(T handler, Inventory playerInventory, Component title, ResourceLocation background) {
		super(handler, playerInventory, title);
		this.background = background;
		this.imageHeight = 166;
	}
	
	@Override
	protected void init() {
		super.init();
		this.colorSelectionWidget = new ColorSelectionWidget(getGuiLeft() + 113, getGuiTop() + 55, getGuiLeft() + 139, getGuiTop() + 25, this.menu.getBlockEntity());
		this.colorSelectionWidget.setChangedListener(this);
		addRenderableWidget(this.colorSelectionWidget);
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		int titleX = (imageWidth - font.width(title)) / 2;
		int titleY = 6;
		drawContext.drawString(this.font, this.title.getVisualOrderText(), titleX, titleY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
		drawContext.drawString(this.font, this.playerInventoryTitle, BaseInkScreenHandler.PLAYER_INVENTORY_START_X, BaseInkScreenHandler.PLAYER_INVENTORY_START_Y - 10, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		guiGraphics.blit(background, getGuiLeft(), getGuiTop(), 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		super.render(guiGraphics, mouseX, mouseY, delta);
		guiGraphics.blit(background, getGuiLeft() + 52, getGuiTop() + 18, 176, 0, 46, 46); // gauge blanket
		renderTooltip(guiGraphics, mouseX, mouseY);
	}
	
	@Override
	public void accept(Optional<Holder<InkColor>> inkColor) {
		BaseInkBlockEntity<?> inkBlockEntity = this.menu.getBlockEntity();
		inkBlockEntity.setSelectedColor(inkColor);
		PacketDistributor.sendToServer(new InkColorSelectedC2SPayload(inkColor));
	}
	
	@Override
	protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
		super.renderTooltip(guiGraphics, x, y);
		
		if (this.colorSelectionWidget.isMouseOver(x, y)) {
			this.colorSelectionWidget.drawMouseoverTooltip(guiGraphics, x, y);
		}
	}
	
}