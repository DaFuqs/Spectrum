package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.SpectrumTier;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GenericSpectrumContainerScreen extends HandledScreen<GenericSpectrumContainerScreenHandler> {

    private static final Identifier TIER_1_TEXTURE_6x9 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/generic_54_tier_1.png");
    private static final Identifier TIER_2_TEXTURE_6x9 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/generic_54_tier_2.png");
    private static final Identifier TIER_3_TEXTURE_6x9 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/generic_54_tier_3.png");

    private final int rows;
    private final SpectrumTier tier;

    public GenericSpectrumContainerScreen(GenericSpectrumContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.rows = handler.getRows();
        this.tier = handler.getTier();

        this.backgroundHeight = 114 + this.rows * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        // draw "title" and "inventory" texts
        int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2; // 8;
        int titleY = 7;
        Text title = this.title;
        int inventoryX = 8;

        this.textRenderer.draw(matrices, title, titleX, titleY, 4210752);
        this.textRenderer.draw(matrices, this.playerInventoryTitle, inventoryX, playerInventoryTitleY, 4210752);
    }


    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getBackground(rows, tier));
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
        this.drawTexture(matrices, i, j + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
    }

    private Identifier getBackground(int rows, SpectrumTier tier) {
        switch (tier) {
            case TIER1 -> {
                return TIER_1_TEXTURE_6x9;
            }
            case TIER2 -> {
                return TIER_2_TEXTURE_6x9;
            }
            default -> {
                return TIER_3_TEXTURE_6x9;
            }
        }
    }

}
