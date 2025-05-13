package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.recipe.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.input.*;

public class EnchanterInventory extends SimpleInventory {
	
	public EnchanterInventory() {
		super(10);
	}
	
	public EnchanterInventory(ItemStack... items) {
		super(items);
	}
	
	public RecipeInput createInput() {
		return new SimpleRecipeInput(this.heldStacks);
	}
	
	public void rotate() {
		ItemStack stack2 = getStack(2);
		ItemStack stack3 = getStack(3);

		setStack(2, getStack(4));
		setStack(3, getStack(5));
		setStack(4, getStack(6));
		setStack(5, getStack(7));
		setStack(6, getStack(8));
		setStack(7, getStack(9));
		setStack(8, stack2);
		setStack(9, stack3);
	}
	
	public void mirror() {
		ItemStack stack2 = getStack(2);
		ItemStack stack4 = getStack(4);
		ItemStack stack6 = getStack(6);
		ItemStack stack8 = getStack(8);

		setStack(2, getStack(3));
		setStack(4, getStack(5));
		setStack(6, getStack(7));
		setStack(8, getStack(9));

		setStack(3, stack2);
		setStack(5, stack4);
		setStack(7, stack6);
		setStack(9, stack8);
	}
	
}
