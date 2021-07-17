package de.dafuqs.pigment;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

    public static IntStream getAvailableSlots(Inventory inventory, Direction side) {
        return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory)inventory).getAvailableSlots(side)) : IntStream.range(0, inventory.size());
    }

    public static boolean canExtract(Inventory inv, ItemStack stack, int slot, Direction facing) {
        return !(inv instanceof SidedInventory) || ((SidedInventory)inv).canExtract(slot, stack, facing);
    }

    private static boolean isInventoryEmpty(Inventory inv, Direction facing) {
        return getAvailableSlots(inv, facing).allMatch((i) -> inv.getStack(i).isEmpty());
    }

    @Nullable
    public static Inventory getInventoryAt(World world, double x, double y, double z) {
        Inventory inventory = null;
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof InventoryProvider) {
            inventory = ((InventoryProvider)block).getInventory(blockState, world, blockPos);
        } else if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof Inventory) {
                inventory = (Inventory)blockEntity;
                if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
                }
            }
        }

        if (inventory == null) {
            List<Entity> list = world.getOtherEntities(null, new Box(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntityPredicates.VALID_INVENTORIES);
            if (!list.isEmpty()) {
                inventory = (Inventory)list.get(world.random.nextInt(list.size()));
            }
        }

        return inventory;
    }

}
