package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.entity.player.*;
import net.minecraft.screen.slot.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class FilteringScreen extends HandledScreen<FilteringScreenHandler> {

    public static final Identifier BACKGROUND = SpectrumCommon.locate("textures/gui/container/filter.png");
    public static final int STRIP_OFFSET = 144;
    public static final int STRIP_HEIGHT = 16;
    public static final int PLAYER_OFFSET = 55;
    public static final int PLAYER_HEIGHT = 89;
    public static final int BASE_FILTER_HEIGHT = 44;
    private final int rows;

    public FilteringScreen(FilteringScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.rows = handler.getRows() - 1;
        this.backgroundHeight = BASE_FILTER_HEIGHT + PLAYER_HEIGHT + ((int) Math.round(rows * 1.5) * STRIP_HEIGHT);
    }

    @Override
    protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
        // draw "title" and "inventory" texts
        int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        int titleY = 6;
        Text title = this.title;
        int inventoryX = 8;
        int intInventoryY = 41 + ((int) Math.round(rows * 1.5) * STRIP_HEIGHT);

        drawContext.drawText(this.textRenderer, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
        drawContext.drawText(this.textRenderer, this.playerInventoryTitle, inventoryX, intInventoryY, RenderHelper.GREEN_COLOR, false);
    }

    @Override
    protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        drawContext.drawTexture(BACKGROUND, x, y, 0, 0, backgroundWidth, BASE_FILTER_HEIGHT);
        var drawRows = (int) Math.round(rows * 1.5);
        for (int i = 0; i < drawRows; i++) {
            drawContext.drawTexture(BACKGROUND, x, y + BASE_FILTER_HEIGHT + i * STRIP_HEIGHT, 0, STRIP_OFFSET, backgroundWidth, STRIP_HEIGHT);
        }

        drawContext.drawTexture(BACKGROUND, x, y + BASE_FILTER_HEIGHT + drawRows * STRIP_HEIGHT, 0, PLAYER_OFFSET, backgroundWidth, PLAYER_HEIGHT);

        for (int i = 0; i < Math.min(handler.filterInventory.size(), handler.drawnSlots); i++) {
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