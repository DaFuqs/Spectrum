package de.dafuqs.spectrum.items.tooltip;

import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;

public class CraftingTabletTooltipData implements TooltipData {
	
	private final ItemStack itemStack;
	private final Text description;
	
	public CraftingTabletTooltipData(Recipe recipe) {
		this.itemStack = recipe.getOutput();
		this.description = Text.translatable("item.spectrum.crafting_tablet.tooltip.recipe", this.itemStack.getCount(), this.itemStack.getName().getString());
	}
	
	public ItemStack getItemStack() {
		return this.itemStack;
	}
	
	public Text getDescription() {
		return this.description;
	}
	
}
