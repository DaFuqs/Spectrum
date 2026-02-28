package de.dafuqs.spectrum.blocks.bottomless_bundle;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Minimal Forge-native storage equivalent to Fabric's SingleVariantStorage<ItemVariant> that this mod used.
 * Exposes public fields `variant` and `amount` so existing call-sites (the builder etc.) can access them directly.
 */
public final class BottomlessItemHandler implements IItemHandler {
	
	private final long capacity;
	private final boolean deletesOverflow;
	
	/**
	 * The template variant as a single-item ItemStack
	 * Count always expected to be 1 when non-empty
	 */
	public ItemStack variant = ItemStack.EMPTY;
	public long amount = 0L;
	
	public BottomlessItemHandler(long capacity, boolean deletesOverflow) {
		this.capacity = capacity;
		this.deletesOverflow = deletesOverflow;
	}
	// returns the amount that could get inserted
	private long insert(ItemStack insertedVariant, long maxAmount) {
		if (!isItemValid(0, insertedVariant)) return 0L;
		long capacity = getCapacity();
		long space = capacity - this.amount;
		if (space <= 0L) return 0L;
		long toInsert = Math.min(space, maxAmount);
		if (this.variant.isEmpty()) {
			// Lock template to one copy of the item
			this.variant = insertedVariant.copyWithCount(1);
		}
		this.amount += toInsert;
		return deletesOverflow ? maxAmount : toInsert;
	}
	
	public long getCapacity() {
		return this.capacity;
	}
	
	@Override
	public int getSlots() {
		return 1;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return variant.copyWithCount((int) Math.min(variant.getMaxStackSize(), this.amount));
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		
		if (!isItemValid(slot, stack))
			return stack;
		
		long insertedAmount = insert(stack, stack.getCount());
		if (insertedAmount == stack.getCount()) {
			return ItemStack.EMPTY;
		}
		
		stack.setCount((int) (stack.getCount() - insertedAmount));
		return stack;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0) {
			return ItemStack.EMPTY;
		}
		
		long amountToExtract = Math.min(amount, this.amount);
		if (amountToExtract <= 0L) {
			return ItemStack.EMPTY;
		}
		
		this.amount -= amountToExtract;
		return this.variant.copyWithCount(amount);
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return Item.ABSOLUTE_MAX_STACK_SIZE;
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack toInsert) {
		// must be an item that can be stored & same item type/components as existing template (if set)
		if (toInsert.isEmpty()) return false;
		if (!toInsert.getItem().canFitInsideContainerItems()) return false;
		if (this.variant.isEmpty()) return true;
		return ItemStack.isSameItemSameComponents(this.variant, toInsert);
	}
}
