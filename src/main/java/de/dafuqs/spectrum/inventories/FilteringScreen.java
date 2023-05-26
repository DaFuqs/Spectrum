package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.screen.slot.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class FilteringScreen extends HandledScreen<FilteringScreenHandler> {

    public static final Identifier BACKGROUND = SpectrumCommon.locate("textures/gui/container/filter.png");

    public FilteringScreen(FilteringScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.backgroundHeight = 133;
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        // draw "title" and "inventory" texts
        int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        int titleY = 6;
        Text title = this.title;
        int inventoryX = 8;
        int intInventoryY = 41;

        this.textRenderer.draw(matrices, title, titleX, titleY, 3289650);
        this.textRenderer.draw(matrices, this.playerInventoryTitle, inventoryX, intInventoryY, 3289650);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        for (int i = 0; i < handler.filterInventory.size(); i++) {
            Slot s = handler.getSlot(i);
            drawTexture(matrices, x + s.x - 1, y + s.y - 1, 176, 0, 18, 18);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

}