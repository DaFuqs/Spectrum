package de.dafuqs.spectrum.items.tooltip;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class CraftingTabletTooltipComponent extends SpectrumTooltipComponent {

    private final ItemStack itemStack;
    private final OrderedText description;

    public CraftingTabletTooltipComponent(CraftingTabletTooltipData data) {
        this.itemStack = data.getItemStack();
        this.description = data.getDescription().asOrderedText();
    }

    public int getHeight() {
        return 20 + 4;
    }

    public int getWidth(TextRenderer textRenderer) {
        return textRenderer.getWidth(this.description) + 28;
    }

    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z, TextureManager textureManager) {
        int n = x + 1;
        int o = y + 1;
        this.drawSlot(n, o, 0, itemStack, textRenderer, matrices, itemRenderer, z, textureManager);
        this.drawOutline(x, y, 1, 1, matrices, z, textureManager);
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix4f, VertexConsumerProvider.Immediate immediate) {
        textRenderer.draw(this.description, (float) x + 26, (float) y + 6, 11053224, true, matrix4f, immediate, false, 0, 15728880);
    }

    private int getColumns() {
        return Math.max(2, (int) Math.ceil(Math.sqrt((double) 1 + 1.0D)));
    }

}
