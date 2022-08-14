package de.dafuqs.spectrum.items.tooltip;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.Text;

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
