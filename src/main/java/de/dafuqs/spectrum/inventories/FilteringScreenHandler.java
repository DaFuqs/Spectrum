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

import java.util.function.*;

public class FilteringScreenHandler extends ScreenHandler {

	protected final World world;
	protected FilterConfigurable filterConfigurable;
	protected final Inventory filterInventory;

	public FilteringScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
		this(SpectrumScreenHandlerTypes.FILTERING, syncId, playerInventory,
				(handler) -> FilterConfigurable.getFilterInventoryFromPacketHandler(syncId, playerInventory, packetByteBuf, handler));
	}

	public FilteringScreenHandler(int syncId, PlayerInventory playerInventory, FilterConfigurable filterConfigurable) {
		this(SpectrumScreenHandlerTypes.FILTERING, syncId, playerInventory,
				(handler) -> FilterConfigurable.getFilterInventoryFromItemsHandler(syncId, playerInventory, filterConfigurable.getItemFilters(), handler));
		this.filterConfigurable = filterConfigurable;
	}
	
	protected FilteringScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Function<ScreenHandler, Inventory> filterInventoryFactory) {
		super(type, syncId);
		this.world = playerInventory.player.getWorld();
		this.filterInventory = filterInventoryFactory.apply(this);

		// filter slots
		int startX = (176 / 2) - (filterInventory.size() + 1) * 9;
		for (int k = 0; k < filterInventory.size(); ++k) {
			this.addSlot(new FilterSlot(filterInventory, k, startX + k * 23, 18));
		}

		// player inventory slots
		int i = 52;
		for (int j = 0; j < 3; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, j * 18 + i));
			}
		}
		// player hotbar
		for (int j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 58 + i));
		}

	}
	
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}

	public Inventory getInventory() {
		return null;
	}
	
	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
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
