package de.dafuqs.spectrum.items.tooltip;

import net.fabricmc.api.*;
import net.minecraft.client.font.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;

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
	public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
		int n = x + 1;
		int o = y + 1;
		this.drawSlot(n, o, 0, itemStack, textRenderer, matrices, itemRenderer, z);
		this.drawOutline(x, y, 1, 1, matrices, z);
	}
	
	@Override
	public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix4f, VertexConsumerProvider.Immediate immediate) {
		textRenderer.draw(this.description, (float) x + 26, (float) y + 6, 11053224, true, matrix4f, immediate, false, 0, 15728880);
	}
	
}
