package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.recipe.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

public class EnchanterInventory extends SimpleContainer {
	
	public EnchanterInventory() {
		super(10);
	}
	
	public EnchanterInventory(ItemStack... items) {
		super(items);
	}
	
	public RecipeInput createInput() {
		return new SimpleRecipeInput(this.getItems());
	}
	
	public void rotate() {
		ItemStack stack2 = getItem(2);
		ItemStack stack3 = getItem(3);
		
		setItem(2, getItem(4));
		setItem(3, getItem(5));
		setItem(4, getItem(6));
		setItem(5, getItem(7));
		setItem(6, getItem(8));
		setItem(7, getItem(9));
		setItem(8, stack2);
		setItem(9, stack3);
	}
	
	public void mirror() {
		ItemStack stack2 = getItem(2);
		ItemStack stack4 = getItem(4);
		ItemStack stack6 = getItem(6);
		ItemStack stack8 = getItem(8);
		
		setItem(2, getItem(3));
		setItem(4, getItem(5));
		setItem(6, getItem(7));
		setItem(8, getItem(9));
		
		setItem(3, stack2);
		setItem(5, stack4);
		setItem(7, stack6);
		setItem(9, stack8);
	}
	
}
