package de.dafuqs.spectrum.blocks.bottomless_bundle;

import net.minecraft.world.item.*;
import net.neoforged.neoforge.items.*;

import java.util.*;

public class BottomlessItemHandler implements IItemHandler, Iterable<ItemStack> {
	
	private final long capacity;
	private final boolean deletesOverflow;
	private final boolean locked;
	private ItemStack variant;
	private long count;
	
	public BottomlessItemHandler(long capacity, boolean deletesOverflow, boolean locked, ItemStack variant, long count) {
		this.capacity = capacity;
		this.deletesOverflow = deletesOverflow;
		this.locked = locked;
		this.variant = variant;
		this.count = count;
	}
	
	public ItemStack variant() {
		return variant;
	}
	
	public long count() {
		return count;
	}
	
	public long capacity() {
		return capacity;
	}
	
	public boolean locked() {
		return locked;
	}
	
	public boolean deletesOverflow() {
		return deletesOverflow;
	}
	
	public void setStack(ItemStack bundledStack) {
		this.variant = bundledStack.copyWithCount(1);
		this.count = bundledStack.getCount();
	}
	
	// returns the amount that could get inserted
	private long insert(ItemStack insertedVariant, long maxAmount, boolean simulate) {
		if (!isItemValid(0, insertedVariant)) return 0L;
		long capacity = getCapacity();
		long space = capacity - this.count;
		if (!deletesOverflow && space <= 0L) return 0L;
		long toInsert = Math.min(space, maxAmount);
		this.variant = insertedVariant.copyWithCount(1);
		if(!simulate) {
			this.count += toInsert;
		}
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
		return variant.copyWithCount((int) Math.min(variant.getMaxStackSize(), this.count));
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		
		if (!isItemValid(slot, stack))
			return stack;
		
		long insertedAmount = insert(stack, stack.getCount(), simulate);
		if(insertedAmount == 0) {
			return stack;
		}
		if (insertedAmount == stack.getCount()) {
			return ItemStack.EMPTY;
		}
		
		return stack.copyWithCount(stack.getCount() - (int) insertedAmount);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0) {
			return ItemStack.EMPTY;
		}
		
		int amountToExtract = (int) Math.min(amount, this.count);
		if (amountToExtract <= 0L) {
			return ItemStack.EMPTY;
		}
		
		if(!simulate) {
			this.count -= amountToExtract;
		}
		return this.variant.copyWithCount(amountToExtract);
	}
	
	public ItemStack extractSingleStack() {
		return extractItem(0, variant.getMaxStackSize(), false);
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return Item.ABSOLUTE_MAX_STACK_SIZE;
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack toInsert) {
		// must be an item that can be stored & same item type/components as existing template (if set)
		if (toInsert.isEmpty()) return false;
		if (!toInsert.canFitInsideContainerItems()) return false;
		
		if(this.locked()) {
			return ItemStack.isSameItemSameComponents(this.variant, toInsert);
		} else {
			if(this.isEmpty()) {
				return true;
			}
		}
		return ItemStack.isSameItemSameComponents(this.variant, toInsert);
	}
	
	public boolean isEmpty() {
		return this.variant.isEmpty() || this.count == 0L;
	}
	
	@Override
	public Iterator<ItemStack> iterator() {
		return new Iterator<>() {
			
			@Override
			public boolean hasNext() {
				return !isEmpty();
			}
			
			@Override
			public ItemStack next() {
				return extractSingleStack();
			}
			
		};
	}
}
