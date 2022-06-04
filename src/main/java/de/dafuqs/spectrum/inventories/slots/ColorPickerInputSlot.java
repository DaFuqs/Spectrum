package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.recipe.ink_converting.InkConvertingRecipe;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ColorPickerInputSlot extends Slot {
	
	public ColorPickerInputSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
		return super.canInsert(stack) && InkConvertingRecipe.isInput(stack.getItem());
	}
	
}
