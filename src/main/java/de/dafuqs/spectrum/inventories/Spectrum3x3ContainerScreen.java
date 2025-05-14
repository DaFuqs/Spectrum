package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

public class Spectrum3x3ContainerScreen extends AbstractContainerScreen<Spectrum3x3ContainerScreenHandler> {
	
	private static final ResourceLocation TIER_1_TEXTURE_3x3 = SpectrumCommon.locate("textures/gui/container/generic_3x3_tier_1.png");
	private static final ResourceLocation TIER_2_TEXTURE_3x3 = SpectrumCommon.locate("textures/gui/container/generic_3x3_tier_2.png");
	private static final ResourceLocation TIER_3_TEXTURE_3x3 = SpectrumCommon.locate("textures/gui/container/generic_3x3_tier_3.png");
	
	private final ResourceLocation backgroundTexture;
	
	public Spectrum3x3ContainerScreen(Spectrum3x3ContainerScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
		ScreenBackgroundVariant tier = handler.getTier();
		this.backgroundTexture = getBackground(tier);
	}
	
	@Override
	protected void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		this.renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		this.renderTooltip(drawContext, mouseX, mouseY);
	}
	
	/**
	 * Draw with custom background
	 */
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		drawContext.blit(backgroundTexture, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
	
	@Contract(pure = true)
	private ResourceLocation getBackground(@NotNull ScreenBackgroundVariant tier) {
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
