package de.dafuqs.spectrum.items.tooltip;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

@Environment(EnvType.CLIENT)
public class VoidBundleTooltipComponent extends SpectrumTooltipComponent {
	
	private static final int MAX_DISPLAYED_SLOTS = 5;
	private final List<ItemStack> itemStacks;
	
	private final int displayedSlotCount;
	private final boolean drawDots;
	
	public VoidBundleTooltipComponent(VoidBundleTooltipData data) {
		int amount = data.getAmount();
		
		int maxCount = data.getItemStack().getMaxCount();
		double totalStacks = (float) amount / (float) maxCount;
		this.displayedSlotCount = Math.max(2, Math.min(MAX_DISPLAYED_SLOTS + 1, (int) Math.ceil(totalStacks) + 1));
		
		this.itemStacks = DefaultedList.ofSize(5, ItemStack.EMPTY);
		for (int i = 0; i < Math.min(5, displayedSlotCount + 1); i++) {
			ItemStack slotStack = data.getItemStack().copy();
			int stackAmount = Math.min(maxCount, amount - i * maxCount);
			slotStack.setCount(stackAmount);
			this.itemStacks.set(i, slotStack);
		}
		drawDots = totalStacks > MAX_DISPLAYED_SLOTS;
	}
	
	@Override
	public int getHeight() {
		return 20 + 2 + 4;
	}
	
	@Override
	public int getWidth(TextRenderer textRenderer) {
		return this.displayedSlotCount * 20 + 2 + 4;
	}
	
	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
		int n = x + 1;
		int o = y + 1;
		
		for (int i = 0; i < Math.min(MAX_DISPLAYED_SLOTS + 1, displayedSlotCount); i++) {
			if (i == displayedSlotCount - 1) {
				if (displayedSlotCount == MAX_DISPLAYED_SLOTS + 1) {
					if (drawDots) {
						this.drawDottedSlot(n + 5 * 18, o, matrices, z);
					} else {
						this.drawSlot(n + i * 18, o, i, ItemStack.EMPTY, textRenderer, matrices, itemRenderer, z);
					}
				} else {
					if (this.itemStacks.size() - 1 < i) {
						this.drawSlot(n + i * 18, o, i, ItemStack.EMPTY, textRenderer, matrices, itemRenderer, z);
					} else {
						this.drawSlot(n + i * 18, o, i, this.itemStacks.get(i), textRenderer, matrices, itemRenderer, z);
					}
				}
			} else {
				this.drawSlot(n + i * 18, o, i, this.itemStacks.get(i), textRenderer, matrices, itemRenderer, z);
			}
		}
		this.drawOutline(x, y, displayedSlotCount, 1, matrices, z);
	}
	
}
