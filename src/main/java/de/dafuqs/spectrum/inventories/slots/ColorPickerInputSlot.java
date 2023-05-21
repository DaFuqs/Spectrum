package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.recipe.ink_converting.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.slot.*;

public class ColorPickerInputSlot extends Slot {
	
	public ColorPickerInputSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
		return super.canInsert(stack) && InkConvertingRecipe.isInput(stack.getItem());
	}
	
}
