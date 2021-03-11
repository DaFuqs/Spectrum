package de.dafuqs.pigment.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.pigment.PigmentCommon;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AltarScreen extends HandledScreen<AltarScreenHandler> {

    private static final Identifier BACKGROUND = new Identifier(PigmentCommon.MOD_ID, "textures/gui/container/altar.png");

    public AltarScreen(AltarScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.backgroundHeight = 194;
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        // draw "title" and "inventory" texts
        int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2; // 8;
        int titleY = 7;
        Text title = this.title;
        int inventoryX = 8;
        int intInventoryY = 100;

        this.textRenderer.draw(matrices, title, titleX, titleY, 4210752);
        // TODO:
        //this.textRenderer.draw(matrices, this.playerInventory.getName(), inventoryX, intInventoryY, 4210752);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::method_34542);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        boolean isCrafting = this.handler.isCrafting();
        if(isCrafting) {
            int progressWidth = (this.handler).getCraftingProgress();
            // x+y: destination, u+v: original coords in texture file
            this.drawTexture(matrices, x + 88, y + 37, 176, 0, progressWidth + 1, 16);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }


}