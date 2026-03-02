package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.*;

import java.util.*;

public class BottomlessItemHandler implements IItemHandler, Iterable<ItemStack> {
	
	private final long capacity;
	private final boolean deletesOverflow;
	private final boolean locked;
	public ItemStack variant;
	public long count;
	
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
	
	// returns the amount that could get inserted
	private long insert(ItemStack insertedVariant, long maxAmount) {
		if (!isItemValid(0, insertedVariant)) return 0L;
		long capacity = getCapacity();
		long space = capacity - this.count;
		if (space <= 0L) return 0L;
		long toInsert = Math.min(space, maxAmount);
		if (this.variant.isEmpty()) {
			// Lock template to one copy of the item
			this.variant = insertedVariant.copyWithCount(1);
		}
		this.count += toInsert;
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
	public @NotNull ItemStack getStackInSlot(int slot) {
		return variant.copyWithCount((int) Math.min(variant.getMaxStackSize(), this.count));
	}

	@Override
	public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		
		if (!isItemValid(slot, stack))
			return stack;
		
		long insertedAmount = insert(stack, stack.getCount());
		if (insertedAmount == stack.getCount()) {
			return ItemStack.EMPTY;
		}
		
		stack.shrink((int) insertedAmount);
		return stack;
	}
	
	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0) {
			return ItemStack.EMPTY;
		}
		
		long amountToExtract = Math.min(amount, this.count);
		if (amountToExtract <= 0L) {
			return ItemStack.EMPTY;
		}
		
		this.count -= amountToExtract;
		ItemStack result = this.variant.copyWithCount(amount);
		if(this.count <= 0L) {
			this.variant = ItemStack.EMPTY;
		}
		return result;
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
		if (this.isEmpty()) return true;
		return ItemStack.isSameItemSameComponents(this.variant, toInsert);
	}
	
	public boolean isEmpty() {
		return this.variant.isEmpty() || this.count == 0L;
	}
	
	@Override
	public @NotNull Iterator<ItemStack> iterator() {
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
