package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class FilteringScreenHandler extends ScreenHandler {

	protected final World world;
	protected FilterConfigurable filterConfigurable;
	protected final Inventory filterInventory;
	protected final int rows, slotsPerRow, drawnSlots;

	public FilteringScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
		this(SpectrumScreenHandlerTypes.FILTERING, syncId, playerInventory,
				(handler) -> FilterConfigurable.getFilterInventoryWithRowDataFromPacket(syncId, playerInventory, packetByteBuf, handler));
	}

	public FilteringScreenHandler(int syncId, PlayerInventory playerInventory, FilterConfigurable filterConfigurable) {
		this(SpectrumScreenHandlerTypes.FILTERING, syncId, playerInventory,
				(handler) -> new Pair<>(FilterConfigurable.getFilterInventoryFromItemsHandler(syncId, playerInventory, filterConfigurable.getItemFilters(), handler), new Integer[]{
						filterConfigurable.getFilterRows(),
						filterConfigurable.getSlotsPerRow(),
						filterConfigurable.getDrawnSlots()
				}));
		this.filterConfigurable = filterConfigurable;
	}

	protected FilteringScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Function<ScreenHandler, Pair<Inventory, Integer[]>> filterInventoryFactory) {
		super(type, syncId);
		this.world = playerInventory.player.getWorld();
		var pair = filterInventoryFactory.apply(this);
		this.filterInventory = pair.getLeft();
		var slotData = pair.getRight();
		rows = slotData[0];
		slotsPerRow = slotData[1];
		drawnSlots = slotData[2];
		int nonObligatoryRows = rows - 1;
		var slotCount = Math.min(filterInventory.size(), drawnSlots);

		// filter slots
		slotDraw: {
			int startX = (176 / 2) - (slotsPerRow + 1) * 9;
			int index = 0;
			for (int i = 0; i < rows; i++) {
				for (int k = 0; k < slotsPerRow; ++k) {
					if (index == slotCount)
						break slotDraw;
					this.addSlot(new FilterSlot(filterInventory, index, startX + k * 23, 18 + i * (FilteringScreen.STRIP_HEIGHT + 8)));
					index++;
				}
			}
		}

		// player inventory slots
		int i = 52 + ((int) Math.round(nonObligatoryRows * 1.5) * FilteringScreen.STRIP_HEIGHT);
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

	public int getRows() {
		return rows;
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
				filterConfigurable.setFilterItem(getIndex(), ItemVariant.of(heldStack));
			}
			return super.onClicked(heldStack, type, player);
		}
	}

}
