package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.RenderHelper;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public class SpectrumGenericContainerScreen extends HandledScreen<GenericSpectrumContainerScreenHandler> {
	
	private static final Identifier TIER_1_TEXTURE_6x9 = SpectrumCommon.locate("textures/gui/container/generic_54_tier_1.png");
	private static final Identifier TIER_2_TEXTURE_6x9 = SpectrumCommon.locate("textures/gui/container/generic_54_tier_2.png");
	private static final Identifier TIER_3_TEXTURE_6x9 = SpectrumCommon.locate("textures/gui/container/generic_54_tier_3.png");
	
	private final int rows;
	private final Identifier backgroundTexture;
	
	public SpectrumGenericContainerScreen(GenericSpectrumContainerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.rows = handler.getRows();
		this.backgroundTexture = getBackground(rows, handler.getTier());
		
		this.backgroundHeight = 114 + this.rows * 18;
		this.playerInventoryTitleY = this.backgroundHeight - 94;
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		this.renderBackground(drawContext);
		super.render(drawContext, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(drawContext, mouseX, mouseY);
	}
	
	@Override
	protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2; // 8;
		int titleY = 7;
		Text title = this.title;
		int inventoryX = 8;

		var tr = this.textRenderer;

		drawContext.drawText(tr, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawText(tr, this.playerInventoryTitle, inventoryX, playerInventoryTitleY, RenderHelper.GREEN_COLOR, false);
	}
	
	
	@Override
	protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		drawContext.drawTexture(backgroundTexture, i, j, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
		drawContext.drawTexture(backgroundTexture, i, j + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
	}
	
	@Contract(pure = true)
	private Identifier getBackground(int rows, @NotNull ScreenBackgroundVariant tier) {
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
