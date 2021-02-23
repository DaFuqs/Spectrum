package de.dafuqs.pigment.inventories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class AutoCraftingInventory extends AutoInventory {

    List<ItemStack> inputInventory;

    public AutoCraftingInventory() {
        super();
        inputInventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    }

    public void setInputInventory(List<ItemStack> inputInventory) {
        this.inputInventory = inputInventory;
    }

    @Override
    public int size() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inputInventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public int getWidth() {
        return 3;
    }

}
