package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.recipe.*;
import net.minecraft.world.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class ColorPickerInputSlot extends Slot {
	
	public ColorPickerInputSlot(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return super.mayPlace(stack) && InkConvertingRecipe.isInput(stack.getItem());
	}
	
}
