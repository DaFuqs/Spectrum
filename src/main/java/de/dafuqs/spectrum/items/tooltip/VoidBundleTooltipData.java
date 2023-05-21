package de.dafuqs.spectrum.items.tooltip;

import net.minecraft.client.item.*;
import net.minecraft.item.*;

public class VoidBundleTooltipData implements TooltipData {
	
	private final ItemStack itemStack;
	private final int amount;
	
	public VoidBundleTooltipData(ItemStack itemStack, int amount) {
		this.itemStack = itemStack;
		this.amount = amount;
	}
	
	public ItemStack getItemStack() {
		return this.itemStack;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
}
