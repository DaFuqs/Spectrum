package de.dafuqs.pigment.inventories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

public class AutoCraftingInventory extends CraftingInventory {

    public enum AutoCraftingMode {
        TwoXTwo,
        ThreeXTree
    }

    private AutoCraftingMode autoCraftingMode;
    ItemStack compactingItemStack;

    public AutoCraftingInventory() {
        super(null, 3, 3);
        this.compactingItemStack = ItemStack.EMPTY;
        this.autoCraftingMode = AutoCraftingMode.ThreeXTree;
    }

    public void setCompacting(AutoCraftingMode autoCraftingMode, ItemStack itemStack) {
        this.autoCraftingMode = autoCraftingMode;
        this.compactingItemStack = itemStack;
        this.compactingItemStack.setCount(1);
    }

    @Override
    public int size() {
        return getSize() * getSize();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return compactingItemStack;
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
        return getSize();
    }

    @Override
    public int getWidth() {
        return getSize();
    }

    private int getSize() {
        switch (this.autoCraftingMode) {
            case TwoXTwo:
                return 2;
            default:
                return 3;
        }
    }

}
