package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

import javax.annotation.*;

public class SpectrumGenericContainerScreen extends AbstractContainerScreen<GenericSpectrumContainerScreenHandler> {
	
	private static final ResourceLocation TIER_1_TEXTURE_6x9 = SpectrumCommon.locate("textures/gui/container/generic_54_tier_1.png");
	private static final ResourceLocation TIER_2_TEXTURE_6x9 = SpectrumCommon.locate("textures/gui/container/generic_54_tier_2.png");
	private static final ResourceLocation TIER_3_TEXTURE_6x9 = SpectrumCommon.locate("textures/gui/container/generic_54_tier_3.png");
	
	private final int rows;
	private final ResourceLocation backgroundTexture;
	
	public SpectrumGenericContainerScreen(GenericSpectrumContainerScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
		this.rows = handler.getRowCount();
		this.backgroundTexture = getBackground(rows, handler.getTier());
		
		this.imageHeight = 114 + this.rows * 18;
		this.inventoryLabelY = this.imageHeight - 94;
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		this.renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		this.renderTooltip(drawContext, mouseX, mouseY);
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (imageWidth - font.width(title)) / 2; // 8;
		int titleY = 7;
		Component title = this.title;
		int inventoryX = 8;
		
		var tr = this.font;
		
		drawContext.drawString(tr, title, titleX, titleY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
		drawContext.drawString(tr, this.playerInventoryTitle, inventoryX, inventoryLabelY, RenderHelper.SPECTRUM_CONTAINER_TEXT_COLOR, false);
	}
	
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		int i = getGuiLeft();
		int j = getGuiTop();
		drawContext.blit(backgroundTexture, i, j, 0, 0, this.imageWidth, this.rows * 18 + 17);
		drawContext.blit(backgroundTexture, i, j + this.rows * 18 + 17, 0, 126, this.imageWidth, 96);
	}
	
	@Contract(pure = true)
	private ResourceLocation getBackground(int rows, ScreenBackgroundVariant tier) {
		switch (tier) {
			case EARLYGAME -> {
				return TIER_1_TEXTURE_6x9;
			}
			case MIDGAME -> {
				return TIER_2_TEXTURE_6x9;
			}
			default -> {
				return TIER_3_TEXTURE_6x9;
			}
		}
	}
	
}
