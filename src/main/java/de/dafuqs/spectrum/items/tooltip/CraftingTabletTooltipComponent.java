package de.dafuqs.spectrum.items.tooltip;

import net.fabricmc.api.*;
import net.minecraft.client.font.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.render.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import org.joml.*;

@Environment(EnvType.CLIENT)
public class CraftingTabletTooltipComponent extends SpectrumTooltipComponent {
	
	private final ItemStack itemStack;
	private final OrderedText description;
	
	public CraftingTabletTooltipComponent(CraftingTabletTooltipData data) {
		this.itemStack = data.getItemStack();
		this.description = data.getDescription().asOrderedText();
	}
	
	@Override
	public int getHeight() {
		return 20 + 4;
	}
	
	@Override
	public int getWidth(TextRenderer textRenderer) {
		return textRenderer.getWidth(this.description) + 28;
	}
	
	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
		int n = x + 1;
		int o = y + 1;
		this.drawSlot(context, n, o, 0, itemStack, textRenderer);
		this.drawOutline(context, x, y, 1, 1);
	}
	
	@Override
	public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix4f, VertexConsumerProvider.Immediate immediate) {
		textRenderer.draw(this.description, (float) x + 26, (float) y + 6, 11053224, true, matrix4f, immediate, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
	}
	
}
