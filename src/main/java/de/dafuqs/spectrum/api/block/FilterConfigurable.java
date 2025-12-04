package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface FilterConfigurable {
	
	List<ItemStack> getItemFilters();
	
	void setFilterItem(int slot, ItemStack item);
	
	default int getFilterRows() {
		return 1;
	}
	
	default int getSlotsPerRow() {
		return 5;
	}
	
	default int getDrawnSlots() {
		return getItemFilters().size();
	}
	
	static void writeFilterNbt(CompoundTag tag, List<ItemStack> filterItems) {
		for (int i = 0; i < filterItems.size(); i++) {
			if (!filterItems.get(i).isEmpty()) {
				CodecHelper.writeNbt(tag, "FilterStack" + i, ItemStack.CODEC, filterItems.get(i));
			}
		}
	}
	
	static void readFilterNbt(CompoundTag tag, List<ItemStack> filterItems) {
		for (int i = 0; i < filterItems.size(); i++) {
			if (tag.contains("FilterStack" + i))
				filterItems.set(i, CodecHelper.fromNbt(ItemStack.CODEC, tag.get("FilterStack" + i), null));
		}
	}
	
	static Container getFilterInventoryFromDataClicker(ExtendedData data, ShadowSlotClicker clicker) {
		var size = data.filterItems().size();
		Container inventory = new FilterInventory(clicker, size);
		for (int i = 0; i < size; i++) {
			inventory.setItem(i, data.filterItems().get(i));
		}
		return inventory;
	}
	
	static Container getFilterInventoryFromExtendedData(int syncId, @NotNull Inventory playerInventory, ExtendedData data, @NotNull AbstractContainerMenu handler) {
		final var clicker = new ShadowSlotClicker.FromHandler(handler, playerInventory.player, syncId);
		return getFilterInventoryFromDataClicker(data, clicker);
	}
	
	static Container getFilterInventoryFromItemsClicker(List<ItemStack> items, ShadowSlotClicker clicker) {
		Container inventory = new FilterInventory(clicker, items.size());
		for (int i = 0; i < items.size(); i++) {
			inventory.setItem(i, items.get(i));
		}
		return inventory;
	}
	
	static Container getFilterInventoryFromItemsHandler(int syncId, @NotNull Inventory playerInventory, List<ItemStack> items, @NotNull AbstractContainerMenu thisHandler) {
		final var clicker = new ShadowSlotClicker.FromHandler(thisHandler, playerInventory.player, syncId);
		return getFilterInventoryFromItemsClicker(items, clicker);
	}
	
	// Ensures execution of ShadowSlot.onClicked both on the server and client.
	// Do not use if not required.
	interface ShadowSlotClicker {
		default void clickShadowSlot(int syncId, Slot slot, ItemStack shadowStack) {
			clickShadowSlot(syncId, slot.index, shadowStack);
		}
		
		void clickShadowSlot(int syncId, int id, ItemStack shadowStack);
		
		class FromHandler implements ShadowSlotClicker {
			public final @NotNull AbstractContainerMenu handler;
			public final @NotNull Player player;
			public final int syncId;
			
			public FromHandler(@NotNull AbstractContainerMenu screenHandler, @NotNull Player player, int syncId) {
				this.handler = screenHandler;
				this.player = player;
				this.syncId = syncId;
			}
			
			@Override
			public void clickShadowSlot(int syncId, @Nullable Slot slot, ItemStack shadowStack) {
				if (this.syncId != syncId || !(slot instanceof ShadowSlot shadowSlot)) return;
				if (!shadowSlot.onClicked(shadowStack, ClickAction.PRIMARY, player)) return;
				
				// Sync with server
				if (player.level().isClientSide()) {
					PacketDistributor.sendToServer(new SetShadowSlotPayload(syncId, slot.index, shadowStack));
				}
			}
			
			@Override
			public void clickShadowSlot(int syncId, int id, ItemStack shadowStack) {
				this.clickShadowSlot(syncId, handler.getSlot(id), shadowStack);
			}
		}
	}
	
	// Contains the slot clicker.
	class FilterInventory extends SimpleContainer {
		private final @NotNull FilterConfigurable.ShadowSlotClicker clicker;
		
		public FilterInventory(@NotNull FilterConfigurable.ShadowSlotClicker slotClicker, int size) {
			super(size);
			this.clicker = slotClicker;
		}
		
		public @NotNull FilterConfigurable.ShadowSlotClicker getClicker() {
			return clicker;
		}
	}
	
	static void writeScreenOpeningData(RegistryFriendlyByteBuf buf, FilterConfigurable configurable) {
		writeScreenOpeningData(buf, configurable.getItemFilters(), configurable.getFilterRows(), configurable.getSlotsPerRow(), configurable.getDrawnSlots());
	}
	
	static void writeScreenOpeningData(RegistryFriendlyByteBuf buf, List<ItemStack> filterItems, int rows, int slotsPerRow, int drawnSlots) {
		buf.writeInt(filterItems.size());
		ItemStack.LIST_STREAM_CODEC.encode(buf, filterItems);
		buf.writeInt(rows);
		buf.writeInt(slotsPerRow);
		buf.writeInt(drawnSlots);
	}
	
	default boolean hasEmptyFilter() {
		return getItemFilters().stream().allMatch(ItemStack::isEmpty);
	}
	
	record ExtendedData(List<ItemStack> filterItems, int rows, int slotsPerRow, int drawnSlots) {
		
		public ExtendedData(FilterConfigurable configurable) {
			this(configurable.getItemFilters(), configurable.getFilterRows(), configurable.getSlotsPerRow(), configurable.getDrawnSlots());
		}
		
		public static final StreamCodec<RegistryFriendlyByteBuf, ExtendedData> PACKET_CODEC = StreamCodec.composite(
				ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), ExtendedData::filterItems,
				ByteBufCodecs.VAR_INT, ExtendedData::rows,
				ByteBufCodecs.VAR_INT, ExtendedData::slotsPerRow,
				ByteBufCodecs.VAR_INT, ExtendedData::drawnSlots,
				ExtendedData::new
		);
		
	}
	
	record ExtendedDataWithPos(BlockPos pos, ExtendedData data) {
		
		public ExtendedDataWithPos(BlockPos pos, FilterConfigurable configurable) {
			this(pos, new ExtendedData(configurable.getItemFilters(), configurable.getFilterRows(), configurable.getSlotsPerRow(), configurable.getDrawnSlots()));
		}
		
		public static final StreamCodec<RegistryFriendlyByteBuf, ExtendedDataWithPos> PACKET_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC, c -> c.pos,
				ExtendedData.PACKET_CODEC, c -> c.data,
				ExtendedDataWithPos::new
		);
		
	}
	
}
