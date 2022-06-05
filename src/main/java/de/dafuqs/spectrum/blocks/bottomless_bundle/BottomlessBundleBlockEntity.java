package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.items.magic_items.BottomlessBundleItem;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BottomlessBundleBlockEntity extends BlockEntity implements SidedInventory {
	
	protected final static int INPUT_SLOT_ID = 0;
	protected final static int OUTPUT_SLOT_ID = 1;
	protected ItemStack bottomlessBundleStack;
	protected ItemStack acceptedItemStack; // caching for performance and to prevent the bundle being emptied
	// and then filled with different item type
	protected ItemStack currentOutputStack;
	
	public BottomlessBundleBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.BOTTOMLESS_BUNDLE, pos, state);
		this.bottomlessBundleStack = ItemStack.EMPTY;
		this.acceptedItemStack = ItemStack.EMPTY;
		this.currentOutputStack = ItemStack.EMPTY;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		NbtList nbtList = nbt.getList("Items", 10);
		for (int i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			int j = nbtCompound.getByte("Slot") & 255;
			if (j == 0) {
				this.bottomlessBundleStack = ItemStack.fromNbt(nbtCompound);
				this.acceptedItemStack = BottomlessBundleItem.getFirstBundledStack(bottomlessBundleStack);
			} else {
				this.currentOutputStack = ItemStack.fromNbt(nbtCompound);
			}
		}
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		bottomlessBundleStack.writeNbt(nbt);
		currentOutputStack.writeNbt(nbt);
		
		NbtList nbtList = new NbtList();
		
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putByte("Slot", (byte) 0);
		bottomlessBundleStack.writeNbt(nbtCompound);
		nbtList.add(nbtCompound);
		
		NbtCompound nbtCompound2 = new NbtCompound();
		nbtCompound2.putByte("Slot", (byte) 1);
		currentOutputStack.writeNbt(nbtCompound2);
		nbtList.add(nbtCompound2);
		
		nbt.put("Items", nbtList);
	}
	
	public void setVoidBundle(@NotNull ItemStack itemStack) {
		if (itemStack.getItem() instanceof BottomlessBundleItem) {
			this.bottomlessBundleStack = itemStack;
			this.acceptedItemStack = BottomlessBundleItem.getFirstBundledStack(itemStack);
		}
	}
	
	public ItemStack retrieveVoidBundle() {
		if (this.bottomlessBundleStack.isEmpty()) {
			return SpectrumItems.BOTTOMLESS_BUNDLE.getDefaultStack();
		} else {
			BottomlessBundleItem.addToBundle(this.bottomlessBundleStack, this.currentOutputStack);
			return this.bottomlessBundleStack;
		}
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		if (side == Direction.DOWN) {
			return new int[]{OUTPUT_SLOT_ID};
		} else {
			return new int[]{INPUT_SLOT_ID};
		}
	}
	
	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return slot == 0 && stack.getItem().canBeNested()
				&& (this.acceptedItemStack.isEmpty() || ItemStack.canCombine(stack, this.acceptedItemStack))
				&& BottomlessBundleItem.getStoredAmount(this.bottomlessBundleStack) + this.currentOutputStack.getCount() < BottomlessBundleItem.getMaxStoredAmount(this.bottomlessBundleStack);
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return slot == 1;
	}
	
	@Override
	public int size() {
		return 2;
	}
	
	@Override
	public boolean isEmpty() {
		return BottomlessBundleItem.getStoredAmount(this.bottomlessBundleStack) == 0 && this.currentOutputStack.getCount() == 0;
	}
	
	@Override
	public ItemStack getStack(int slot) {
		if (slot == 0) {
			return ItemStack.EMPTY;
		} else {
			this.markDirty();
			if (currentOutputStack.isEmpty()) {
				Optional<ItemStack> newStack = BottomlessBundleItem.removeFirstBundledStack(this.bottomlessBundleStack);
				if (newStack.isPresent()) {
					currentOutputStack = newStack.get();
				} else {
					currentOutputStack = ItemStack.EMPTY;
				}
			}
			return currentOutputStack;
		}
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		if (slot == 0) {
			return ItemStack.EMPTY;
		} else {
			this.markDirty();
			return this.currentOutputStack.split(amount);
		}
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		if (slot == 1) {
			ItemStack removedStack = this.currentOutputStack;
			this.currentOutputStack = ItemStack.EMPTY;
			if (!removedStack.isEmpty()) {
				this.markDirty();
			}
			return removedStack;
		} else {
			return ItemStack.EMPTY;
		}
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		if (slot == 0) {
			if (this.acceptedItemStack.isEmpty()) {
				this.acceptedItemStack = stack;
			}
			BottomlessBundleItem.addToBundle(this.bottomlessBundleStack, stack);
			this.markDirty();
		} else {
			this.currentOutputStack = stack;
		}
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}
	
	@Override
	public void clear() {
	
	}
	
}
