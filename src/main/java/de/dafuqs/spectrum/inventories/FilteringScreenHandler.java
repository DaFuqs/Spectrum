package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class FilteringScreenHandler extends ScreenHandler {

    protected final World world;
    protected FilterConfigurable filterConfigurable;
    protected Inventory filterInventory;

    public FilteringScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        this(SpectrumScreenHandlerTypes.FILTERING, syncId, playerInventory, FilterConfigurable.getFilterInventoryFromPacket(packetByteBuf));
    }

    public FilteringScreenHandler(int syncId, PlayerInventory playerInventory, FilterConfigurable filterConfigurable) { // called via PastelNodeBlockEntity
        this(SpectrumScreenHandlerTypes.FILTERING, syncId, playerInventory, FilterConfigurable.getFilterInventoryFromItems(filterConfigurable.getItemFilters()));
        this.filterConfigurable = filterConfigurable;
    }

    protected FilteringScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory filterInventory) {
        super(type, syncId);
        this.world = playerInventory.player.world;
        this.filterInventory = filterInventory;

        int i = -4 * 18;

        // sucking chest slots
        int j;
        int k;

        // player inventory slots
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 112 + 19 + j * 18 + i));
            }
        }

        // player hotbar
        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 170 + 19 + i));
        }

        // filter slots
        for (k = 0; k < filterInventory.size(); ++k) {
            this.addSlot(new FilterSlot(filterInventory, k, 8 + k * 23, 18));
        }
    }


    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    public Inventory getInventory() {
        return null;
    }

    public void close(PlayerEntity player) {
        super.close(player);
    }

    public FilterConfigurable getFilterConfigurable() {
        return this.filterConfigurable;
    }

    protected class FilterSlot extends ShadowSlot {

        public FilterSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean onClicked(ItemStack heldStack, ClickType type, PlayerEntity player) {
            if (!world.isClient && filterConfigurable != null) {
                filterConfigurable.setFilterItem(getIndex(), heldStack.getItem());
            }
            return super.onClicked(heldStack, type, player);
        }
    }

}
