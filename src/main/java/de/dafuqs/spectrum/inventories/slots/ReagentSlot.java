package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopReactingRecipe;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ReagentSlot extends Slot {
	
	public ReagentSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
		return super.canInsert(stack) && PotionWorkshopReactingRecipe.isReagent(stack.getItem());
	}
	
}
