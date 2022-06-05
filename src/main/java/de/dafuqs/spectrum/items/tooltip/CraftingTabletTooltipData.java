package de.dafuqs.spectrum.items.tooltip;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.TranslatableText;

public class CraftingTabletTooltipData implements TooltipData {
	
	private final ItemStack itemStack;
	private final TranslatableText description;
	
	public CraftingTabletTooltipData(Recipe recipe) {
		this.itemStack = recipe.getOutput();
		this.description = new TranslatableText("item.spectrum.crafting_tablet.tooltip.recipe", this.itemStack.getCount(), this.itemStack.getName().getString());
	}
	
	public ItemStack getItemStack() {
		return this.itemStack;
	}
	
	public TranslatableText getDescription() {
		return this.description;
	}
	
}
