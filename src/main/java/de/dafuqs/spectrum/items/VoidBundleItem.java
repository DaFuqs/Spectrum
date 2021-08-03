package de.dafuqs.spectrum.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class VoidBundleItem extends BundleItem {

    private static final int MAX_STORED_AMOUNT = Integer.MAX_VALUE;

    public VoidBundleItem(Item.Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (dropAllBundledItems(itemStack, user)) {
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }

    /**
     * When the bundle is clicked onto another stack
     */
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            ItemStack itemStack = slot.getStack();
            if (itemStack.isEmpty()) {
                removeFirstBundledStack(stack).ifPresent((removedStack) -> {
                    addToBundle(stack, slot.insertStack(removedStack));
                });
            } else if (itemStack.getItem().canBeNested()) {
                int amountToAdd = MAX_STORED_AMOUNT - getStoredAmount(stack) - itemStack.getCount();
                addToBundle(stack, slot.takeStackRange(itemStack.getCount(), amountToAdd, player));
            }

            return true;
        }
    }

    /**
     * When an itemStack is right clicked onto the bundle
     */
    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            if (otherStack.isEmpty()) {
                Optional<ItemStack> removedItemStack = removeFirstBundledStack(stack);
                Objects.requireNonNull(cursorStackReference);
                removedItemStack.ifPresent(cursorStackReference::set);
            } else {
                otherStack.decrement(addToBundle(stack, otherStack));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canBeNested() {
        return false;
    }

    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + 12 * stack.getCount() / MAX_STORED_AMOUNT, 13);
    }

    private static int addToBundle(ItemStack bundle, ItemStack stackToBundle) {
        if (!stackToBundle.isEmpty() && stackToBundle.getItem().canBeNested()) {
            int bundleOccupancy = getStoredAmount(bundle);
            int roomLeft = Math.min(stackToBundle.getCount(), (MAX_STORED_AMOUNT - bundleOccupancy) / stackToBundle.getCount());
            if (roomLeft > 0) {
                ItemStack stackInBundle = getFirstBundledStack(bundle);
                if (stackInBundle.isEmpty()) {
                    stackInBundle = stackToBundle.copy();
                    stackInBundle.setCount(roomLeft);
                    bundleStack(bundle, stackInBundle);
                    return roomLeft;
                } else if (ItemStack.canCombine(stackInBundle, stackToBundle)) {
                    stackInBundle.increment(roomLeft);
                    bundleStack(bundle, stackInBundle);
                    return roomLeft;
                }
            }
        }
        return 0;
    }

    private static Optional<ItemStack> removeFirstBundledStack(ItemStack voidBundleStack) {
        ItemStack removedStack = getFirstBundledStack(voidBundleStack);
        if (removedStack.isEmpty()) {
            return Optional.empty();
        } else {
            removeBundledStackAmount(voidBundleStack, removedStack.getCount());
            return Optional.of(removedStack);
        }
    }

    private static boolean dropAllBundledItems(ItemStack voidBundleStack, PlayerEntity player) {
        int storedAmount = getStoredAmount(voidBundleStack);
        int currentAmount = storedAmount;
        if(currentAmount > 0) {
            ItemStack storedStack = getFirstBundledStack(voidBundleStack);
            while (currentAmount > 0) {
                int stackCount = Math.min(currentAmount, storedStack.getMaxCount());
                storedStack.setCount(stackCount);
                currentAmount -= stackCount;
                player.dropItem(storedStack, true);
            }
            removeBundledStackAmount(voidBundleStack, storedAmount);
            return true;
        } else {
            return false;
        }
    }

    private static ItemStack getFirstBundledStack(ItemStack voidBundleStack) {
        NbtCompound nbtCompound = voidBundleStack.getTag();
        if (nbtCompound == null) {
            return ItemStack.EMPTY;
        } else {
            return getFirstBundledStack(nbtCompound);
        }
    }

    private static ItemStack getFirstBundledStack(NbtCompound nbtCompound) {
        NbtCompound storedItemCompound = nbtCompound.getCompound("StoredStack");

        int storedAmount = storedItemCompound.getInt("Count");
        ItemStack itemStack = new ItemStack(Registry.ITEM.get(new Identifier(storedItemCompound.getString("ID"))));
        int stackAmount = Math.min(storedAmount, itemStack.getMaxCount());
        itemStack.setCount(stackAmount);

        if (storedItemCompound.contains("Tag", 10)) {
            itemStack.setTag(storedItemCompound.getCompound("Tag"));
        }
        if (itemStack.getItem().isDamageable()) {
            itemStack.setDamage(itemStack.getDamage());
        }
        return itemStack;
    }

    private static void bundleStack(ItemStack voidBundleStack, ItemStack storedItemStack) {
        NbtCompound voidBundleCompound = voidBundleStack.getOrCreateTag();
        NbtCompound storedItemCompound = new NbtCompound();

        Identifier identifier = Registry.ITEM.getId(storedItemStack.getItem());
        storedItemCompound.putString("ID", identifier.toString());
        storedItemCompound.putInt("Count", storedItemStack.getCount());
        if (storedItemStack.getTag() != null) {
            storedItemCompound.put("Tag", storedItemStack.getTag().copy());
        }

        voidBundleCompound.put("StoredStack", storedItemCompound);
        voidBundleStack.setTag(voidBundleCompound);
    }

    private static int getStoredAmount(ItemStack voidBundleStack) {
        NbtCompound voidBundleCompound = voidBundleStack.getOrCreateTag();
        if(voidBundleCompound.contains("StoredStack")) {
            NbtCompound storedStackCompound = voidBundleCompound.getCompound("StoredStack");
            if(storedStackCompound.contains("Count")) {
                return storedStackCompound.getInt("Count");
            }
        }
        return 0;
    }

    private static void removeBundledStackAmount(ItemStack voidBundleStack, int amount) {
        int storedAmount = getStoredAmount(voidBundleStack);

        NbtCompound voidBundleCompound = voidBundleStack.getOrCreateTag();
        if (voidBundleCompound.contains("StoredStack")) {
            int remainingCount = storedAmount - amount;
            if (remainingCount > 0) {
                NbtCompound storedStackCompound = voidBundleCompound.getCompound("StoredStack");
                storedStackCompound.putInt("Count", remainingCount);
                voidBundleCompound.put("StoredStack", storedStackCompound);
            } else {
                voidBundleCompound.remove("StoredStack");
            }
            voidBundleStack.setTag(voidBundleCompound);
        }
    }

}