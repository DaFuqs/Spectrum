package de.dafuqs.spectrum.blocks.stonesetting_workshop;

import de.dafuqs.spectrum.items.CrystalGraceItem;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.id.incubus_core.be.IncubusBaseBE;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class StonesettingWorkshopBlockEntity extends IncubusBaseBE {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);
    public static final int GRACE_SLOTS_END = 7, SHARD_SLOT = 8, INFUSEE_SLOT = 9, MIDNIGHT_SLOT = 10;

    private int filledGraceSlots;

    public StonesettingWorkshopBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntities.STONESETTING_WORKSHOP, pos, state);
    }

    /**
     * Attempts to insert an item, returns whether that insertion was successful.
     */
    public boolean tryInsertItem(ItemStack stack, PlayerEntity player, Hand hand) {

        if (world == null)
            return false;

        var item = stack.getItem();
        var success = false;

        // Try to insert crystal graces in one of the first 8 slots
        if (item instanceof CrystalGraceItem) {

            success = tryInsertGrace(stack);
            if (success)
                clearHand(player, hand);

        }

        //Any crystal shards in the 9th slot?
        if (stack.isIn(SpectrumItemTags.GEMSTONE_SHARDS) && inventory.get(SHARD_SLOT).isEmpty()) {

            inventory.set(SHARD_SLOT, new ItemStack(stack.getItem()));
            stack.decrement(1);

            success = true;
        }

        //If this is what we are infusing, try to put it in the last slot
        if (item instanceof ToolItem || item instanceof ArmorItem && inventory.get(INFUSEE_SLOT).isEmpty()) {

            inventory.set(INFUSEE_SLOT, stack);
            clearHand(player, hand);

            success = true;
        }

        if(!world.isClient())
            sync();
        markDirty();

        return success;
    }

    /**
     * Oppositely, tries to extract items
     */

    public boolean tryExtractItem(PlayerEntity player, Hand hand) {

        if (world == null)
            return false;

        var swing = false;

        if (player.isSneaking() && !inventory.get(INFUSEE_SLOT).isEmpty()) {
            player.setStackInHand(hand, inventory.get(INFUSEE_SLOT));
            inventory.set(INFUSEE_SLOT, ItemStack.EMPTY);
            swing = true;
        }

        graces: {
            if (!player.isSneaking()) {

                if (!inventory.get(SHARD_SLOT).isEmpty()) {

                    swing = true;
                    break graces;
                }

                player.setStackInHand(hand, tryExtractGrace());
                if (player.getStackInHand(hand).isEmpty())
                    swing = true;
            }
        }

        if (!world.isClient())
            sync();
        markDirty();

        return swing;
    }

    private void clearHand(PlayerEntity player, Hand hand) {
        player.setStackInHand(hand, ItemStack.EMPTY);
    }

    private boolean tryInsertGrace(ItemStack stack) {
        filledGraceSlots = 0;

        for (int i = 0; i <= GRACE_SLOTS_END; i++) {

            if (!inventory.get(i).isEmpty()) {
                filledGraceSlots++;
                continue;
            }

            inventory.set(i, stack);
            filledGraceSlots++;
            return true;
        }
        return false;
    }

    private ItemStack tryExtractGrace() {

        if (filledGraceSlots == 0)
            return ItemStack.EMPTY;

        var extractedGrace = ItemStack.EMPTY;

        for (int i = GRACE_SLOTS_END; i >= 0; i--) {
            var slot = inventory.get(i);

            if (!slot.isEmpty()) {
                extractedGrace = slot;
                inventory.set(i, ItemStack.EMPTY);
                filledGraceSlots--;
                break;
            }
        }

        return extractedGrace;
    }

    DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public int getFilledGraceSlots() {
        return filledGraceSlots;
    }

    @Override
    public void save(NbtCompound nbt) {
        super.save(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("filledGraceSlots", filledGraceSlots);
    }

    @Override
    public void load(NbtCompound nbt) {
        super.load(nbt);
        Inventories.readNbt(nbt, inventory);
        filledGraceSlots = nbt.getInt("filledGraceSlots");
    }

    @Override
    public void saveClient(NbtCompound nbt) {
        save(nbt);
    }

    @Override
    public void loadClient(NbtCompound nbt) {
        load(nbt);
    }
}
