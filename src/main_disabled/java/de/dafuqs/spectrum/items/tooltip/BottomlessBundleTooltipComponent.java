package de.dafuqs.spectrum.items.tooltip;

import de.dafuqs.spectrum.api.gui.*;
import net.fabricmc.api.*;
import net.minecraft.client.gui.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class BottomlessBundleTooltipComponent implements SpectrumTooltipComponent {
	
	private static final int MAX_DISPLAYED_SLOTS = 5;
	private final List<ItemStack> itemStacks;
	
	private final int displayedSlotCount;
	private final boolean drawDots;
	
	public BottomlessBundleTooltipComponent(BottomlessBundleTooltipData data) {
		long amount = data.amount();
		
		long maxCount = data.variant().getItem().getDefaultMaxStackSize();
		double totalStacks = (float) amount / (float) maxCount;
		this.displayedSlotCount = Math.max(2, Math.min(MAX_DISPLAYED_SLOTS + 1, (int) Math.ceil(totalStacks) + 1));
		
		this.itemStacks = NonNullList.withSize(5, ItemStack.EMPTY);
		for (int i = 0; i < Math.min(5, displayedSlotCount + 1); i++) {
			ItemStack slotStack = data.variant().toStack();
			var stackAmount = Math.min(Math.min(maxCount, amount - i * maxCount), Integer.MAX_VALUE);
			slotStack.setCount((int) stackAmount);
			this.itemStacks.set(i, slotStack);
		}
		drawDots = totalStacks > MAX_DISPLAYED_SLOTS;
	}
	
	@Override
	public int getHeight() {
		return 20 + 2 + 4;
	}
	
	@Override
	public int getWidth(Font textRenderer) {
		return displayedSlotCount * 20 + 2 + 4;
	}
	
	@Override
	public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
		int n = x + 1;
		int o = y + 1;
		
		for (int i = 0; i < Math.min(MAX_DISPLAYED_SLOTS + 1, displayedSlotCount); i++) {
			if (i == displayedSlotCount - 1) {
				if (displayedSlotCount == MAX_DISPLAYED_SLOTS + 1) {
					if (drawDots) {
						SpectrumTooltipComponent.drawDottedSlot(context, n + 5 * 18, o);
					} else {
						SpectrumTooltipComponent.drawSlot(context, n + i * 18, o, i, ItemStack.EMPTY, textRenderer);
					}
				} else {
					if (itemStacks.size() - 1 < i) {
						SpectrumTooltipComponent.drawSlot(context, n + i * 18, o, i, ItemStack.EMPTY, textRenderer);
					} else {
						SpectrumTooltipComponent.drawSlot(context, n + i * 18, o, i, itemStacks.get(i), textRenderer);
					}
				}
			} else {
				SpectrumTooltipComponent.drawSlot(context, n + i * 18, o, i, itemStacks.get(i), textRenderer);
			}
		}
		SpectrumTooltipComponent.drawOutline(context, x, y, displayedSlotCount, 1);
	}
	
}
