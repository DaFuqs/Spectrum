package de.dafuqs.spectrum.items.tooltip;

import de.dafuqs.spectrum.api.gui.*;
import net.fabricmc.api.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.item.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PresentTooltipComponent implements SpectrumTooltipComponent {
	
	private final List<ItemStack> itemStacks;
	
	public PresentTooltipComponent(PresentTooltipData data) {
		this.itemStacks = data.itemStacks();
	}
	
	@Override
	public int getHeight() {
		return 20 + 2 + 4;
	}
	
	@Override
	public int getWidth(Font textRenderer) {
		return this.itemStacks.size() * 20 + 2 + 4;
	}
	
	@Override
	public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
		int n = x + 1;
		int o = y + 1;
		for (int i = 0; i < this.itemStacks.size(); i++) {
			SpectrumTooltipComponent.drawSlot(context, n + i * 18, o, i, this.itemStacks.get(i), textRenderer);
		}
		SpectrumTooltipComponent.drawOutline(context, x, y, this.itemStacks.size(), 1);
	}
	
}
