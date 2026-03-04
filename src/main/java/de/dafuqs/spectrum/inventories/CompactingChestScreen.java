package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

public class CompactingChestScreen extends AbstractContainerScreen<CompactingChestScreenHandler> {
	
	public static final ResourceLocation BACKGROUND = SpectrumCommon.locate("textures/gui/container/compacting_chest.png");
	
	public CompactingChestScreen(CompactingChestScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 178;
	}
	
	@Override
	protected void init() {
		super.init();
		setupInputFields();
	}
	
	protected void setupInputFields() {
		int x = (this.width - this.imageWidth) / 2 + 3;
		int y = (this.height - this.imageHeight) / 2 + 3;
		
		addWidget(Button.builder(Component.literal("Mode"), this::craftingModeButtonPressed)
				.size(16, 16)
				.pos(x + 154, y + 6)
				.build());
	}
	
	private void craftingModeButtonPressed(Button buttonWidget) {
		menu.toggleMode();
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (imageWidth - font.width(title)) / 2; // 8;
		int titleY = 6;
		Component title = this.title;
		int inventoryX = 8;
		int intInventoryY = 83;
		drawContext.drawString(this.font, title, titleX, titleY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
		drawContext.drawString(this.font, this.playerInventoryTitle, inventoryX, intInventoryY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		drawContext.blit(BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);
		
		// the selected crafting mode
		drawContext.blit(BACKGROUND, x + 154, y + 6, 176, 16 * menu.getCraftingMode().ordinal(), 16, 16);
	}
	
	@Override
	public void render(@NotNull GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		
		if (mouseX > leftPos + 153 && mouseX < leftPos + 153 + 16 && mouseY > topPos + 5 && mouseY < topPos + 5 + 16) {
			drawContext.renderTooltip(this.font, Component.translatable("block.spectrum.compacting_chest.toggle_crafting_mode"), mouseX, mouseY);
		} else {
			renderTooltip(drawContext, mouseX, mouseY);
		}
	}
	
}