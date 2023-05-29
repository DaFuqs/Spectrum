package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlackHoleChestScreenHandler extends ScreenHandler {
	
	protected static final int ROWS = 3;
	
	protected final World world;
	private final Inventory inventory;
	protected BlackHoleChestBlockEntity blackHoleChestBlockEntity;
	protected Inventory filterInventory;
	
	public BlackHoleChestScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
		this(syncId, playerInventory, packetByteBuf.readBlockPos(), FilterConfigurable.getFilterInventoryFromPacket(packetByteBuf));
	}
	
	private BlackHoleChestScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos readBlockPos, Inventory filterInventory) {
		this(SpectrumScreenHandlerTypes.BLACK_HOLE_CHEST, syncId, playerInventory, new SimpleInventory(BlackHoleChestBlockEntity.INVENTORY_SIZE), filterInventory);
		BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(readBlockPos);
		if (blockEntity instanceof BlackHoleChestBlockEntity blackHoleChestBlockEntity) {
			this.blackHoleChestBlockEntity = blackHoleChestBlockEntity;
		}
	}

	public BlackHoleChestScreenHandler(int syncId, PlayerInventory playerInventory, BlackHoleChestBlockEntity blackHoleChestBlockEntity) {
		this(SpectrumScreenHandlerTypes.BLACK_HOLE_CHEST, syncId, playerInventory, blackHoleChestBlockEntity, FilterConfigurable.getFilterInventoryFromItems(blackHoleChestBlockEntity.getItemFilters()));
		this.blackHoleChestBlockEntity = blackHoleChestBlockEntity;
		this.filterInventory = FilterConfigurable.getFilterInventoryFromItems(blackHoleChestBlockEntity.getItemFilters());
	}

	protected BlackHoleChestScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory filterInventory) {
		super(type, syncId);
		this.inventory = inventory;
		this.world = playerInventory.player.world;
		this.filterInventory = filterInventory;

		checkSize(inventory, BlackHoleChestBlockEntity.INVENTORY_SIZE);
		inventory.onOpen(playerInventory.player);

		int i = (ROWS - 4) * 18;
		
		// sucking chest slots
		int j;
		int k;
		for (j = 0; j < ROWS; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 26 + 16 + j * 18));
			}
		}
		
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
		
		// experience provider slot
		this.addSlot(new StackFilterSlot(inventory, BlackHoleChestBlockEntity.EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT, 152, 18, SpectrumItems.KNOWLEDGE_GEM));
		
		// filter slots
		for (k = 0; k < BlackHoleChestBlockEntity.ITEM_FILTER_SLOT_COUNT; ++k) {
			this.addSlot(new SuckingChestFilterSlot(filterInventory, k, 8 + k * 23, 18));
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}
	
	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index < ROWS * 9) {
				if (!this.insertItem(itemStack2, ROWS * 9, this.slots.size() - 6, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 0, ROWS * 9, false)) {
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
	
	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		this.inventory.onClose(player);
	}

	public BlackHoleChestBlockEntity getBlockEntity() {
		return this.blackHoleChestBlockEntity;
	}

	protected class SuckingChestFilterSlot extends ShadowSlot {

		public SuckingChestFilterSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean onClicked(ItemStack heldStack, ClickType type, PlayerEntity player) {
			if (blackHoleChestBlockEntity != null) {
				blackHoleChestBlockEntity.setFilterItem(getIndex(), heldStack.getItem());
			}
			return super.onClicked(heldStack, type, player);
		}
	}
	
}
