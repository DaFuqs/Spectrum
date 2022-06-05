package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.ProgressionStage;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Spectrum3x3ContainerScreen extends HandledScreen<Spectrum3x3ContainerScreenHandler> {
	
	private static final Identifier TIER_1_TEXTURE_3x3 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/generic_3x3_tier_1.png");
	private static final Identifier TIER_2_TEXTURE_3x3 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/generic_3x3_tier_2.png");
	private static final Identifier TIER_3_TEXTURE_3x3 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/generic_3x3_tier_3.png");
	
	private final Identifier backgroundTexture;
	
	public Spectrum3x3ContainerScreen(Spectrum3x3ContainerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		ProgressionStage tier = handler.getTier();
		this.backgroundTexture = getBackground(tier);
	}
	
	protected void init() {
		super.init();
		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
	/**
	 * Draw with custom background
	 */
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, backgroundTexture);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
	
	@Contract(pure = true)
	private Identifier getBackground(@NotNull ProgressionStage tier) {
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
