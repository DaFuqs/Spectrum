package de.dafuqs.pigment.inventories;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class InventoryHelper {

    public static Pair<Integer, List<ItemStack>> getStackCountInInventory(ItemStack itemStack, List<ItemStack> inventory) {
        List<ItemStack> foundStacks = new ArrayList<>();
        int count = 0;
        for(ItemStack inventoryStack : inventory) {
            if(inventoryStack.isItemEqual(itemStack)) {
                foundStacks.add(inventoryStack);
                count += inventoryStack.getCount();
            }
        }
        return new Pair(count, foundStacks);
    }

    public static boolean existsStackInInventory(ItemStack itemStack, List<ItemStack> inventory) {
        for(ItemStack inventoryStack : inventory) {
            if(inventoryStack.isItemEqual(itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean addToInventory(List<ItemStack> itemStacks, List<ItemStack> inventory, boolean test) {
        List<ItemStack> additionStacks = new ArrayList<>();
        for(ItemStack itemStack : itemStacks) {
            additionStacks.add(itemStack.copy());
        }

        for(int i = 0; i < inventory.size(); i++) {
            ItemStack currentStack = inventory.get(i);
            for(ItemStack additionStack : additionStacks) {
                boolean doneStuff = false;
                if (additionStack.getCount() > 0) {
                    if (currentStack.isEmpty()) {
                        int maxStackCount = currentStack.getMaxCount();
                        int maxAcceptCount = Math.min(additionStack.getCount(), maxStackCount);

                        if (!test) {
                            ItemStack newStack = additionStack.copy();
                            newStack.setCount(maxAcceptCount);
                            inventory.set(i, newStack);
                        }
                        additionStack.setCount(additionStack.getCount() - maxAcceptCount);
                        doneStuff = true;
                    } else if (additionStack.isItemEqual(currentStack)) {
                        // add to stack;
                        int maxStackCount = currentStack.getMaxCount();
                        int canAcceptCount = maxStackCount - currentStack.getCount();

                        if (canAcceptCount > 0) {
                            if (!test) {
                                inventory.get(i).increment(Math.min(additionStack.getCount(), canAcceptCount));
                            }
                            if (canAcceptCount >= additionStack.getCount()) {
                                additionStack.setCount(0);
                            } else {
                                additionStack.setCount(additionStack.getCount() - canAcceptCount);
                            }
                            doneStuff = true;
                        }
                    }

                    // if there were changes: check if all stacks have count 0
                    if(doneStuff) {
                        boolean allEmpty = true;
                        for(ItemStack itemStack : additionStacks) {
                            if (itemStack.getCount() > 0) {
                                allEmpty = false;
                                break;
                            }
                        }
                        if(allEmpty) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean removeFromInventory(ItemStack removeItemStack, List<ItemStack> inventory) {
        int removeItemStackCount = removeItemStack.getCount();
        for(int i = 0; i < inventory.size(); i++) {
            ItemStack currentStack = inventory.get(i);
            if(removeItemStack.isItemEqual(currentStack)) {
                int currentStackCount = currentStack.getCount();
                if(currentStackCount >= removeItemStackCount) {
                    currentStack.decrement(removeItemStackCount);
                    inventory.set(i, currentStack);
                    removeItemStackCount = 0;
                } else {
                    removeItemStackCount -= currentStackCount;
                    inventory.set(i, ItemStack.EMPTY);
                }
            }
            if(removeItemStackCount == 0) {
                return true;
            }
        }
        return false;
    }
}
