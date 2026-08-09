package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.recipe.color_picker.*;
import net.minecraft.world.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public class ColorPickerInputSlot extends Slot {
	
	public ColorPickerInputSlot(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		return super.mayPlace(stack) && InkConvertingRecipe.isInput(stack.getItem());
	}
	
}
