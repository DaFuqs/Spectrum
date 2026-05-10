package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.apache.commons.lang3.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public interface FilterConfigurable {
	
	default Object2BooleanMap<TagKey<Item>> getFilteredTags() {
		return Object2BooleanMaps.emptyMap();
	}
	
	default boolean onlyDenyListTags() {
		return true;
	}
	
	default void setOnlyDenyListTags(boolean onlyDenyListTags) {
	}
	
	default boolean acceptsItem(Item item) {
		if (item == null || item.equals(Items.AIR)) {
			return false;
		}
		
		if (!this.getFilteredTags().isEmpty()) {
			int returnValue = 0;
			// Latest takes precedence. 1 for if it's allowed, -1 for if it's denied.
			for (TagKey<Item> tag : this.getFilteredTags().keySet()) {
				if (item.builtInRegistryHolder().is(tag)) {
					returnValue = this.getFilteredTags().getBoolean(tag) ? 1 : -1;
				}
			}
			if (returnValue != 0) {
				return returnValue == 1;
			}
			// If we only have denyList tags, treat not being in any as am implicit c:everything.
			if (this.onlyDenyListTags()) {
				return true;
			}
		}
		
		boolean allAir = true;
		for (ItemVariant filterItem : this.getItemFilters()) {
			if (filterItem.getItem().equals(item)) {
				return true;
			}
			if (!filterItem.getItem().equals(Items.AIR)) {
				allAir = false;
			}
		}
		return allAir;
	}
	
	// Run on NBT read.
	default void clearFilters() {
		this.getFilteredTags().clear();
		this.setOnlyDenyListTags(true);
	}
	
	default boolean addTagFilteringItem(ItemVariant itemVariant) {
		ItemStack stack = itemVariant.toStack();
		if (!stack.has(DataComponents.CUSTOM_NAME) || !stack.is(SpectrumItemTags.TAG_FILTERING_ITEMS)) {
			this.setOnlyDenyListTags(false);
			return false;
		}
		String name = StringUtils.trim(stack.getHoverName().getString());
		if (StringUtils.equalsAnyIgnoreCase(name, "*", "any", "all", "everything", "c:*", "c:any", "c:all", "c:everything")) {
			this.setOnlyDenyListTags(true);
			return true;
		}
		
		boolean allow = !name.startsWith("!");
		ResourceLocation identifier = ResourceLocation.tryParse(StringUtils.remove(allow ? name : name.substring(1), '#'));
		if (identifier == null) {
			return false;
		}
		
		// Copied from PastelNodeBlockEntity. This entire section could potentially be a candidate to move into its own function.
		TagKey<Item> tag = SpectrumCommon.CACHED_ITEM_TAG_MAP.computeIfAbsent(identifier, tagId -> BuiltInRegistries.ITEM.getTagNames()
				.filter(t -> t.location().equals(tagId))
				.findFirst()
				.orElse(null));
		
		if (tag == null) {
			return false;
		}
		if (allow) {
			this.setOnlyDenyListTags(false);
		}
		return this.getFilteredTags().put(tag, allow);
	}
	
	// Call on change.
	default void updateTagFilteringItems() {
		this.clearFilters();
		this.getItemFilters().forEach(this::addTagFilteringItem);
	}
	
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
	
	static void writeFilterNbt(CompoundTag tag, List<ItemVariant> filterItems) {
		for (int i = 0; i < filterItems.size(); i++) {
			if (!filterItems.get(i).isBlank()) {
				CodecHelper.writeNbt(tag, "FilterStack" + i, ItemVariant.CODEC, filterItems.get(i));
			}
		}
	}
	
	static void readFilterNbt(CompoundTag tag, List<ItemVariant> filterItems) {
		for (int i = 0; i < filterItems.size(); i++) {
			if (tag.contains("FilterStack" + i))
				filterItems.set(i, CodecHelper.fromNbt(ItemVariant.CODEC, tag.get("FilterStack" + i), null));
		}
	}
	
	static Container getFilterInventoryFromDataClicker(ExtendedData data, ShadowSlotClicker clicker) {
		var size = data.filterItems().size();
		Container inventory = new FilterInventory(clicker, size);
		for (int i = 0; i < size; i++) {
			inventory.setItem(i, data.filterItems().get(i).toStack());
		}
		return inventory;
	}
	
	static Container getFilterInventoryFromExtendedData(int syncId, Inventory playerInventory, ExtendedData data, AbstractContainerMenu handler) {
		final var clicker = new ShadowSlotClicker.FromHandler(handler, playerInventory.player, syncId);
		return getFilterInventoryFromDataClicker(data, clicker);
	}
	
	static Container getFilterInventoryFromItemsClicker(List<ItemVariant> items, ShadowSlotClicker clicker) {
		Container inventory = new FilterInventory(clicker, items.size());
		for (int i = 0; i < items.size(); i++) {
			inventory.setItem(i, items.get(i).toStack());
		}
		return inventory;
	}
	
	static Container getFilterInventoryFromItemsHandler(int syncId, Inventory playerInventory, List<ItemVariant> items, AbstractContainerMenu thisHandler) {
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
			public final AbstractContainerMenu handler;
			public final Player player;
			public final int syncId;
			
			public FromHandler(AbstractContainerMenu screenHandler, Player player, int syncId) {
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
					ClientPlayNetworking.send(new SetShadowSlotPayload(syncId, slot.index, shadowStack));
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
		private final FilterConfigurable.ShadowSlotClicker clicker;
		
		public FilterInventory(FilterConfigurable.ShadowSlotClicker slotClicker, int size) {
			super(size);
			this.clicker = slotClicker;
		}
		
		public FilterConfigurable.ShadowSlotClicker getClicker() {
			return clicker;
		}
	}
	
	default boolean hasEmptyFilter() {
		return getItemFilters().stream().allMatch(ItemVariant::isBlank);
	}
	
	record ExtendedData(List<ItemVariant> filterItems, int rows, int slotsPerRow, int drawnSlots) {
		
		public static final StreamCodec<RegistryFriendlyByteBuf, ExtendedData> PACKET_CODEC = StreamCodec.composite(
				ItemVariant.PACKET_CODEC.apply(ByteBufCodecs.list()), ExtendedData::filterItems,
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
