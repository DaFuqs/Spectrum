package de.dafuqs.spectrum.blocks;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;

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

    static Inventory getFilterInventoryFromItems(List<Item> items) {
        Inventory inventory = new SimpleInventory(items.size());
        for (int i = 0; i < items.size(); i++) {
            inventory.setStack(i, items.get(i).getDefaultStack());
        }
        return inventory;
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
