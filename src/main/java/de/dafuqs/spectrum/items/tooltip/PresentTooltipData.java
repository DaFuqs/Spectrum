package de.dafuqs.spectrum.items.tooltip;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;

import java.util.List;

public class PresentTooltipData implements TooltipData {
	
	private final List<ItemStack> itemStacks;
	
	public PresentTooltipData(List<ItemStack> itemStacks) {
		this.itemStacks = itemStacks;
	}
	
	public List<ItemStack> getItemStacks() {
		return this.itemStacks;
	}
	
}
