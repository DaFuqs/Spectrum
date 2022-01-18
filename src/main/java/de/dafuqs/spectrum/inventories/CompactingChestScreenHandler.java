package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.chests.CompactingChestBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CompactingChestScreenHandler extends ScreenHandler {

	protected int ROWS = 3;
	private final Inventory inventory;
	protected final World world;
	protected CompactingChestBlockEntity compactingChestBlockEntity;
	protected AutoCompactingInventory.AutoCraftingMode currentCraftingMode;
	
	
	public CompactingChestScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
		this(syncId, playerInventory, packetByteBuf.readBlockPos(), packetByteBuf.readInt());
	}

	public CompactingChestScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos readBlockPos, int currentCraftingMode) {
		this(SpectrumScreenHandlerTypes.COMPACTING_CHEST, syncId, playerInventory);
		
		BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(readBlockPos);
		if(blockEntity instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
			this.compactingChestBlockEntity = compactingChestBlockEntity;
		}
		this.currentCraftingMode = AutoCompactingInventory.AutoCraftingMode.values()[currentCraftingMode];
	}
	
	public CompactingChestScreenHandler(int syncId, PlayerInventory playerInventory, CompactingChestBlockEntity compactingChestBlockEntity) {
		this(SpectrumScreenHandlerTypes.COMPACTING_CHEST, syncId, playerInventory, compactingChestBlockEntity);
		this.compactingChestBlockEntity = compactingChestBlockEntity;
	}

	protected CompactingChestScreenHandler(ScreenHandlerType<?> type, int i, PlayerInventory playerInventory) {
		this(type, i, playerInventory, new SimpleInventory(27));
	}

	public CompactingChestScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		this(SpectrumScreenHandlerTypes.COMPACTING_CHEST, syncId, playerInventory, inventory);
	}

	protected CompactingChestScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(type, syncId);
		this.inventory = inventory;
		this.world = playerInventory.player.world;

		checkSize(inventory, 27);
		inventory.onOpen(playerInventory.player);
		
		int i = (ROWS - 4) * 18;
		
		int j;
		int k;
		for(j = 0; j < ROWS; ++j) {
			for(k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 26 + j * 18));
			}
		}
		
		for(j = 0; j < 3; ++j) {
			for(k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 112 + j * 18 + i));
			}
		}
		
		for(j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 170 + i));
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
					if(inventory instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
						compactingChestBlockEntity.inventoryChanged();
					}
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 0, this.ROWS * 9, false)) {
				if(inventory instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
					compactingChestBlockEntity.inventoryChanged();
				}
				return ItemStack.EMPTY;
			}
			
			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
		}
		
		if(inventory instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
			compactingChestBlockEntity.inventoryChanged();
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
	
	public CompactingChestBlockEntity getBlockEntity() {
		return this.compactingChestBlockEntity;
	}
	
	public AutoCompactingInventory.AutoCraftingMode getCurrentCraftingMode() {
		return currentCraftingMode;
	}
	
}
