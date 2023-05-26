package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.util.collection.DefaultedList;

public class LockableCraftingResultSlot extends CraftingResultSlot {
	
	private final PlayerEntity player;
	protected final CraftingInventory input;
	protected final int craftingGridStartIndex;
	protected final int craftingGridEndIndex;
	boolean locked;
	
	public LockableCraftingResultSlot(Inventory craftingResultInventory, int index, int x, int y, PlayerEntity player, CraftingInventory input, int craftingGridStartIndex, int craftingGridEndIndex) {
		super(player, input, craftingResultInventory, index, x, y);
		this.player = player;
		this.input = input;
		this.craftingGridStartIndex = craftingGridStartIndex;
		this.craftingGridEndIndex = craftingGridEndIndex;
		
		this.locked = false;
	}
	
	public boolean canTakeItems(PlayerEntity playerEntity) {
		return !locked;
	}
	
	public void lock() {
		this.locked = true;
	}
	
	public void unlock() {
		this.locked = false;
	}
	
	public void onTakeItem(PlayerEntity player, ItemStack stack) {
		this.onCrafted(stack);
		DefaultedList<ItemStack> defaultedList = player.world.getRecipeManager().getRemainingStacks(RecipeType.CRAFTING, this.input, player.world);
		
		for (int i = craftingGridStartIndex; i < craftingGridEndIndex + 1; ++i) {
			ItemStack slotStack = this.input.getStack(i);
			ItemStack remainingStacks = defaultedList.get(i);
			if (!slotStack.isEmpty()) {
				this.input.removeStack(i, 1);
				slotStack = this.input.getStack(i);
			}
			
			if (!remainingStacks.isEmpty()) {
				if (slotStack.isEmpty()) {
					this.input.setStack(i, remainingStacks);
				} else if (ItemStack.areItemsEqual(slotStack, remainingStacks) && ItemStack.areNbtEqual(slotStack, remainingStacks)) {
					remainingStacks.increment(slotStack.getCount());
					this.input.setStack(i, remainingStacks);
				} else if (!this.player.getInventory().insertStack(remainingStacks)) {
					this.player.dropItem(remainingStacks, false);
				}
			}
		}
	}
	
}
