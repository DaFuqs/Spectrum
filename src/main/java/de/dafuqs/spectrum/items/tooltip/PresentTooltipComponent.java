package de.dafuqs.spectrum.items.tooltip;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.List;

@Environment(EnvType.CLIENT)
public class PresentTooltipComponent extends SpectrumTooltipComponent {
	
	private final List<ItemStack> itemStacks;
	
	public PresentTooltipComponent(PresentTooltipData data) {
		this.itemStacks = data.getItemStacks();
	}
	
	@Override
	public int getHeight() {
		return 20 + 2 + 4;
	}
	
	@Override
	public int getWidth(TextRenderer textRenderer) {
		return this.itemStacks.size() * 20 + 2 + 4;
	}
	
	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer) {
		int n = x + 1;
		int o = y + 1;
		for (int i = 0; i < this.itemStacks.size(); i++) {
			this.drawSlot(n + i * 18, o, i, this.itemStacks.get(i), textRenderer, matrices, itemRenderer);
		}
		this.drawOutline(x, y, this.itemStacks.size(), 1, matrices);
	}
	
}
