package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.inventories.slots.ShadowSlot;
import de.dafuqs.spectrum.networking.SpectrumC2SPacketSender;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.registry.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public interface FilterConfigurable {

    List<ItemVariant> getItemFilters();

    void setFilterItem(int slot, ItemVariant item);

    default int getFilterRows() {
        return 1;
    }

    default int getSlotsPerRow() {
        return 5;
    }

    default int getDrawnSlots() {
        return getItemFilters().size();
    }

    static void writeFilterNbt(NbtCompound tag, List<ItemVariant> filterItems) {
        for (int i = 0; i < filterItems.size(); i++) {
			if (!filterItems.get(i).isBlank()) {
				tag.put("FilterStack" + i, filterItems.get(i).toNbt());
			}
        }
    }

    static void readFilterNbt(NbtCompound tag, List<ItemVariant> filterItems) {
        for (int i = 0; i < filterItems.size(); i++) {
            if (tag.contains("FilterStack" + i)) {
				filterItems.set(i, ItemVariant.fromNbt(tag.getCompound("FilterStack" + i)));
            }
        }
    }

    static Inventory getFilterInventoryFromPacketClicker(PacketByteBuf packetByteBuf, ShadowSlotClicker clicker) {
        int size = packetByteBuf.readInt();
        Inventory inventory = new FilterInventory(clicker, size);
        for (int i = 0; i < size; i++) {
            inventory.setStack(i, packetByteBuf.readItemStack());
        }
        return inventory;
    }

    static Pair<Inventory, Integer[]> getFilterInventoryWithRowDataFromPacket(int syncId, @NotNull PlayerInventory playerInventory, PacketByteBuf packetByteBuf, @NotNull ScreenHandler thisHandler) {
        var inventory = getFilterInventoryFromPacketHandler(syncId, playerInventory, packetByteBuf, thisHandler);
        var arr = new Integer[]{
                packetByteBuf.readInt(),
                packetByteBuf.readInt(),
                packetByteBuf.readInt()
        };

        return new Pair<>(inventory, arr);
    }

    static Inventory getFilterInventoryFromPacketHandler(int syncId, @NotNull PlayerInventory playerInventory, PacketByteBuf packetByteBuf, @NotNull ScreenHandler thisHandler) {
        final var clicker = new ShadowSlotClicker.FromHandler(thisHandler, playerInventory.player, syncId);
        return getFilterInventoryFromPacketClicker(packetByteBuf, clicker);
    }

    static Inventory getFilterInventoryFromItemsClicker(List<ItemVariant> items, ShadowSlotClicker clicker) {
        Inventory inventory = new FilterInventory(clicker, items.size());
        for (int i = 0; i < items.size(); i++) {
            inventory.setStack(i, items.get(i).toStack());
        }
        return inventory;
    }

    static Inventory getFilterInventoryFromItemsHandler(int syncId, @NotNull PlayerInventory playerInventory, List<ItemVariant> items, @NotNull ScreenHandler thisHandler) {
        final var clicker = new ShadowSlotClicker.FromHandler(thisHandler, playerInventory.player, syncId);
        return getFilterInventoryFromItemsClicker(items, clicker);
    }

    // Ensures execution of ShadowSlot.onClicked both on the server and client.
    // Do not use if not required.
    interface ShadowSlotClicker {
        default void clickShadowSlot(int syncId, Slot slot, ItemStack shadowStack) {
            clickShadowSlot(syncId, slot.id, shadowStack);
        }

        void clickShadowSlot(int syncId, int id, ItemStack shadowStack);

        class FromHandler implements ShadowSlotClicker {
            public final @NotNull ScreenHandler handler;
            public final @NotNull PlayerEntity player;
            public final int syncId;

            public FromHandler(@NotNull ScreenHandler screenHandler, @NotNull PlayerEntity player, int syncId) {
                this.handler = screenHandler;
                this.player = player;
                this.syncId = syncId;
            }

            @Override
            public void clickShadowSlot(int syncId, @Nullable Slot slot, ItemStack shadowStack) {
                if (this.syncId != syncId || !(slot instanceof ShadowSlot shadowSlot)) return;
                if (!shadowSlot.onClicked(shadowStack, ClickType.LEFT, player)) return;

                // Sync with server
                if (player.getWorld().isClient()) SpectrumC2SPacketSender.sendShadowSlot(syncId, slot.id, shadowStack);
            }

            @Override
            public void clickShadowSlot(int syncId, int id, ItemStack shadowStack) {
                this.clickShadowSlot(syncId, handler.getSlot(id), shadowStack);
            }
        }
    }

    // Contains the slot clicker.
    class FilterInventory extends SimpleInventory {
        private final @NotNull FilterConfigurable.ShadowSlotClicker clicker;

        public FilterInventory(@NotNull FilterConfigurable.ShadowSlotClicker slotClicker, int size) {
            super(size);
            this.clicker = slotClicker;
        }

        public FilterInventory(@NotNull FilterConfigurable.ShadowSlotClicker slotClicker, ItemStack... items) {
            super(items);
            this.clicker = slotClicker;
        }

        public @NotNull FilterConfigurable.ShadowSlotClicker getClicker() {
            return clicker;
        }
    }

    static void writeScreenOpeningData(PacketByteBuf buf, FilterConfigurable configurable) {
        writeScreenOpeningData(buf, configurable.getItemFilters(), configurable.getFilterRows(), configurable.getSlotsPerRow(), configurable.getDrawnSlots());
    }

    static void writeScreenOpeningData(PacketByteBuf buf, List<ItemVariant> filterItems, int rows, int slotsPerRow, int drawnSlots) {
        buf.writeInt(filterItems.size());
        for (ItemVariant filterItem : filterItems) {
            // The difference between just using filterItem.toNbt() is that ItemVariant nbt uses "item" while ItemStack uses "id"
            buf.writeItemStack(filterItem.toStack());
        }
        buf.writeInt(rows);
        buf.writeInt(slotsPerRow);
        buf.writeInt(drawnSlots);
    }

    default boolean hasEmptyFilter() {
        return getItemFilters().stream().allMatch(ItemVariant::isBlank);
    }

}
