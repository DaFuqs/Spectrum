package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
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
    protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
        // draw "title" and "inventory" texts
        int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        int titleY = 6;
        Text title = this.title;
        int inventoryX = 8;
        int intInventoryY = 41;

        drawContext.drawText(this.textRenderer, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
        drawContext.drawText(this.textRenderer, this.playerInventoryTitle, inventoryX, intInventoryY, RenderHelper.GREEN_COLOR, false);
    }

    @Override
    protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawContext.drawTexture(BACKGROUND, x, y, 0, 0, backgroundWidth, backgroundHeight);

        for (int i = 0; i < handler.filterInventory.size(); i++) {
            Slot s = handler.getSlot(i);
            drawContext.drawTexture(BACKGROUND, x + s.x - 1, y + s.y - 1, 176, 0, 18, 18);
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        renderBackground(drawContext);
        super.render(drawContext, mouseX, mouseY, delta);
        drawMouseoverTooltip(drawContext, mouseX, mouseY);
    }

}