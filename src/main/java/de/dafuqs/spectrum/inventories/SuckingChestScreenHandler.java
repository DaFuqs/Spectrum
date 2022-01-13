package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.chests.SuckingChestBlockEntity;
import de.dafuqs.spectrum.inventories.slots.ShadowSlot;
import de.dafuqs.spectrum.inventories.slots.StackFilterSlot;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class SuckingChestScreenHandler extends ScreenHandler {

	protected int ROWS = 3;
	private final Inventory inventory;
	protected final World world;
	protected SuckingChestBlockEntity suckingChestBlockEntity;
	protected Inventory filterInventory;
	
	public SuckingChestScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
		this(syncId, playerInventory, packetByteBuf.readBlockPos(), getFilterInventoryFromPacket(packetByteBuf));
	}

	public SuckingChestScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos readBlockPos, Inventory filterInventory) {
		this(SpectrumScreenHandlerTypes.SUCKING_CHEST, syncId, playerInventory, filterInventory);
		
		BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(readBlockPos);
		if(blockEntity instanceof SuckingChestBlockEntity suckingChestBlockEntity) {
			this.suckingChestBlockEntity = suckingChestBlockEntity;
		}
	}
	
	public SuckingChestScreenHandler(int syncId, PlayerInventory playerInventory, SuckingChestBlockEntity suckingChestBlockEntity) {
		this(SpectrumScreenHandlerTypes.SUCKING_CHEST, syncId, playerInventory, suckingChestBlockEntity, getFilterInventoryFromItems(suckingChestBlockEntity.getItemFilters()));
		this.suckingChestBlockEntity = suckingChestBlockEntity;
		this.filterInventory = getFilterInventoryFromItems(suckingChestBlockEntity.getItemFilters());
	}

	protected SuckingChestScreenHandler(ScreenHandlerType<?> type, int i, PlayerInventory playerInventory, Inventory filterInventory) {
		this(type, i, playerInventory, new SimpleInventory(SuckingChestBlockEntity.INVENTORY_SIZE), filterInventory);
	}

	public SuckingChestScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory filterInventory) {
		this(SpectrumScreenHandlerTypes.SUCKING_CHEST, syncId, playerInventory, inventory, filterInventory);
	}

	protected SuckingChestScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory filterInventory) {
		super(type, syncId);
		this.inventory = inventory;
		this.world = playerInventory.player.world;
		this.filterInventory = filterInventory;
		
		checkSize(inventory, SuckingChestBlockEntity.INVENTORY_SIZE);
		inventory.onOpen(playerInventory.player);
		
		int i = (ROWS - 4) * 18;
		
		// sucking chest slots
		int j;
		int k;
		for(j = 0; j < ROWS; ++j) {
			for(k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 26+16 + j * 18));
			}
		}
		
		// player inventory slots
		for(j = 0; j < 3; ++j) {
			for(k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 112+19 + j * 18 + i));
			}
		}
		
		// player hotbar
		for(j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 170+19 + i));
		}
		
		// inventory provider slot
		this.addSlot(new StackFilterSlot(inventory, SuckingChestBlockEntity.EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT, 152, 18, SpectrumItems.KNOWLEDGE_GEM));
		
		// filter slots
		for(k = 0; k < SuckingChestBlockEntity.ITEM_FILTER_SLOTS; ++k) {
			this.addSlot(new SuckingChestFilterSlot(filterInventory, k, 8 + k * 23, 18));
		}
	}
	
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index < this.ROWS * 9) {
				if (!this.insertItem(itemStack2, this.ROWS * 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 0, this.ROWS * 9, false)) {
				return ItemStack.EMPTY;
			}
			
			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
		}
		
		return itemStack;
	}

	public Inventory getInventory() {
		return this.inventory;
	}
	
	public void close(PlayerEntity player) {
		super.close(player);
		this.inventory.onClose(player);
	}
	
	public SuckingChestBlockEntity getBlockEntity() {
		return this.suckingChestBlockEntity;
	}

	protected static Inventory getFilterInventoryFromPacket(PacketByteBuf packetByteBuf) {
		Inventory inventory = new SimpleInventory(SuckingChestBlockEntity.ITEM_FILTER_SLOTS);
		for(int i = 0; i < SuckingChestBlockEntity.ITEM_FILTER_SLOTS; i++) {
			inventory.setStack(i, Registry.ITEM.get(packetByteBuf.readIdentifier()).getDefaultStack());
		}
		return inventory;
	}

	protected static Inventory getFilterInventoryFromItems(List<Item> items) {
		Inventory inventory = new SimpleInventory(SuckingChestBlockEntity.ITEM_FILTER_SLOTS);
		for(int i = 0; i < SuckingChestBlockEntity.ITEM_FILTER_SLOTS; i++) {
			inventory.setStack(i, items.get(i).getDefaultStack());
		}
		return inventory;
	}
	
	protected class SuckingChestFilterSlot extends ShadowSlot {
		
		public SuckingChestFilterSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}
		
		@Override
		public boolean onClicked(ItemStack heldStack, ClickType type, PlayerEntity player) {
			suckingChestBlockEntity.setFilterItem(getIndex(), heldStack.getItem());
			return super.onClicked(heldStack, type, player);
		}
	}
	
}
