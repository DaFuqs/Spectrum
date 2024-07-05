package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.registry.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface FilterConfigurable {

    List<Item> getItemFilters();

    void setFilterItem(int slot, Item item);

    default void writeFilterNbt(NbtCompound tag, List<Item> filterItems) {
        for (int i = 0; i < filterItems.size(); i++) {
			tag.putString("Filter" + i, Registries.ITEM.getId(filterItems.get(i)).toString());
        }
    }

    default void readFilterNbt(NbtCompound tag, List<Item> filterItems) {
        for (int i = 0; i < filterItems.size(); i++) {
            if (tag.contains("Filter" + i, NbtElement.STRING_TYPE)) {
				filterItems.set(i, Registries.ITEM.get(new Identifier(tag.getString("Filter" + i))));
            }
        }
    }

    static Inventory getFilterInventoryFromPacket(PacketByteBuf packetByteBuf) {
        int size = packetByteBuf.readInt();
        Inventory inventory = new SimpleInventory(size);
        for (int i = 0; i < size; i++) {
			inventory.setStack(i, Registries.ITEM.get(packetByteBuf.readIdentifier()).getDefaultStack());
        }
        return inventory;
    }

    static Inventory getFilterInventoryFromPacketClicker(PacketByteBuf packetByteBuf, ShadowSlotClicker clicker) {
        int size = packetByteBuf.readInt();
        Inventory inventory = new FilterInventory(clicker, size);
        for (int i = 0; i < size; i++) {
            inventory.setStack(i, Registries.ITEM.get(packetByteBuf.readIdentifier()).getDefaultStack());
        }
        return inventory;
    }
    
    static Inventory getFilterInventoryFromPacketHandler(int syncId, @NotNull PlayerInventory playerInventory, PacketByteBuf packetByteBuf, @NotNull ScreenHandler thisHandler) {
        final var clicker = new ShadowSlotClicker.FromHandler(thisHandler, playerInventory.player, syncId);
        return getFilterInventoryFromPacketClicker(packetByteBuf, clicker);
    }

    static Inventory getFilterInventoryFromItems(List<Item> items) {
        Inventory inventory = new SimpleInventory(items.size());
        for (int i = 0; i < items.size(); i++) {
            inventory.setStack(i, items.get(i).getDefaultStack());
        }
        return inventory;
    }
    
    static Inventory getFilterInventoryFromItemsClicker(List<Item> items, ShadowSlotClicker clicker) {
        Inventory inventory = new FilterInventory(clicker, items.size());
        for (int i = 0; i < items.size(); i++) {
            inventory.setStack(i, items.get(i).getDefaultStack());
        }
        return inventory;
    }
    
    static Inventory getFilterInventoryFromItemsHandler(int syncId, @NotNull PlayerInventory playerInventory, List<Item> items, @NotNull ScreenHandler thisHandler) {
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

    static void writeScreenOpeningData(PacketByteBuf buf, List<Item> filterItems) {
        buf.writeInt(filterItems.size());
        for (Item filterItem : filterItems) {
			buf.writeIdentifier(Registries.ITEM.getId(filterItem));
        }
    }

    default boolean hasEmptyFilter() {
        for (Item item : getItemFilters()) {
            if (item != Items.AIR) {
                return false;
            }
        }
        return true;
    }

}
