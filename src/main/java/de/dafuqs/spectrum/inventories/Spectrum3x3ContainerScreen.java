package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public class Spectrum3x3ContainerScreen extends HandledScreen<Spectrum3x3ContainerScreenHandler> {
	
	private static final Identifier TIER_1_TEXTURE_3x3 = SpectrumCommon.locate("textures/gui/container/generic_3x3_tier_1.png");
	private static final Identifier TIER_2_TEXTURE_3x3 = SpectrumCommon.locate("textures/gui/container/generic_3x3_tier_2.png");
	private static final Identifier TIER_3_TEXTURE_3x3 = SpectrumCommon.locate("textures/gui/container/generic_3x3_tier_3.png");
	
	private final Identifier backgroundTexture;
	
	public Spectrum3x3ContainerScreen(Spectrum3x3ContainerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		ScreenBackgroundVariant tier = handler.getTier();
		this.backgroundTexture = getBackground(tier);
	}
	
	@Override
	protected void init() {
		super.init();
		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		this.renderBackground(drawContext);
		super.render(drawContext, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(drawContext, mouseX, mouseY);
	}
	
	/**
	 * Draw with custom background
	 */
	@Override
	protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		drawContext.drawTexture(backgroundTexture, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
	
	@Contract(pure = true)
	private Identifier getBackground(@NotNull ScreenBackgroundVariant tier) {
		switch (tier) {
			case EARLYGAME -> {
				return TIER_1_TEXTURE_3x3;
			}
			case MIDGAME -> {
				return TIER_2_TEXTURE_3x3;
			}
			default -> {
				return TIER_3_TEXTURE_3x3;
			}
		}
	}
	
}
