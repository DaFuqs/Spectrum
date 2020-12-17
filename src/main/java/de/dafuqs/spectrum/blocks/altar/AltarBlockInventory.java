package de.dafuqs.spectrum.blocks.altar;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class AltarBlockInventory implements Inventory {

    private final int size;
    private final DefaultedList<ItemStack> stacks;


    private AltarBlockInventory(int size) {
        this.size = size;
        this.stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
    }

    public static AltarBlockInventory of(int size) {
        return new AltarBlockInventory(size);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.stacks.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot >= 0 && slot < size) {
            return stacks.get(slot);
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot access slot bigger than inventory size");
        }
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = Inventories.splitStack(this.stacks, slot, amount);
        if (!stack.isEmpty()) {
            this.markDirty();
        }
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = stacks.get(slot);

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            stacks.set(slot, ItemStack.EMPTY);
            markDirty();
            return stack;
        }
    }

    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     * @param slot  The inventory slot of which to replace the itemstack.
     * @param stack The replacing itemstack. If the stack is too big for
     *              this inventory ({@link Inventory#getMaxCountPerStack()}),
     *              it gets resized to this inventory's maximum amount.
     */
    @Override
    public void setStack(int slot, ItemStack stack) {
        stacks.set(slot, stack);

        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }

        markDirty();
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */
    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for (int slot = 0; slot < size; ++slot) {
            setStack(slot, ItemStack.EMPTY);
        }
        markDirty();
    }
}
