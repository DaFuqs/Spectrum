package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.function.*;

public class FilteringScreenHandler extends AbstractContainerMenu {
	
	protected final Level world;
	protected FilterConfigurable filterConfigurable;
	protected final Container filterInventory;
	protected final int rows, slotsPerRow, drawnSlots;
	
	// clientside
	public FilteringScreenHandler(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		this(syncId, playerInventory, FilterConfigurable.ExtendedData.PACKET_CODEC.decode(buf), new Integer[]{
				data.data().rows(),
				data.data().slotsPerRow(),
				data.data().drawnSlots()
		});
	}
	
	public FilteringScreenHandler(int syncId, Inventory playerInventory, FilterConfigurable.ExtendedDataWithPos data, Function<AbstractContainerMenu, Tuple<Container, Integer[]>> filterInventoryFactory) {
		this(SpectrumScreenHandlerTypes.FILTERING, syncId, playerInventory, (handler) -> new Tuple<>(FilterConfigurable.getFilterInventoryFromItemsHandler(syncId, playerInventory, data.filterItems(), handler), new Integer[]{
				data.rows(),
				data.slotsPerRow(),
				data.drawnSlots()
		}));
	}
	
	// serverside
	protected FilteringScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, Function<AbstractContainerMenu, Tuple<Container, Integer[]>> filterInventoryFactory) {
		super(type, syncId);
		this.world = playerInventory.player.level();
		this.filterConfigurable = (FilterConfigurable) playerInventory.player.level().getBlockEntity(data.pos());
		var pair = filterInventoryFactory.apply(this);
		this.filterInventory = pair.getA();
		var slotData = pair.getB();
		rows = slotData[0];
		slotsPerRow = slotData[1];
		drawnSlots = slotData[2];
		int nonObligatoryRows = rows - 1;
		var slotCount = Math.min(filterInventory.getContainerSize(), drawnSlots);
		
		// filter slots
		slotDraw: {
			int startX = (176 / 2) - (slotsPerRow + 1) * 9;
			int index = 0;
			for (int i = 0; i < rows; i++) {
				for (int k = 0; k < slotsPerRow; ++k) {
					if (index == slotCount)
						break slotDraw;
					this.addSlot(new FilterSlot(filterConfigurable, filterInventory, index, startX + k * 23, 18 + i * (FilteringScreen.STRIP_HEIGHT + 8)));
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
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}
	
	public Container getInventory() {
		return null;
	}
	
	@Override
	public void removed(Player player) {
		super.removed(player);
	}

}
