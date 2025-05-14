package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class CompactingChestScreenHandler extends AbstractContainerMenu {
	
	private final ContainerData propertyDelegate;
	private final CompactingChestBlockEntity blockEntity;
	protected final int ROWS = 3;
	
	public CompactingChestScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
		this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(pos, SpectrumBlockEntities.COMPACTING_CHEST).orElseThrow(), new SimpleContainerData(1));
	}
	
	public CompactingChestScreenHandler(int syncId, Inventory playerInventory, CompactingChestBlockEntity blockEntity, ContainerData propertyDelegate) {
		super(SpectrumScreenHandlerTypes.COMPACTING_CHEST, syncId);
		
		this.blockEntity = blockEntity;
		this.propertyDelegate = propertyDelegate;
		
		checkContainerSize(blockEntity, 27);
		blockEntity.startOpen(playerInventory.player);
		
		int i = (ROWS - 4) * 18;
		
		int j;
		int k;
		for (j = 0; j < ROWS; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlot(new Slot(blockEntity, k + j * 9, 8 + k * 18, 26 + j * 18));
			}
		}
		
		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 112 + j * 18 + i));
			}
		}
		
		for (j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 170 + i));
		}
		
		this.addDataSlots(propertyDelegate);
	}
	
	@Override
	public boolean stillValid(Player player) {
		return this.blockEntity.stillValid(player);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();
			if (index < this.ROWS * 9) {
				if (!this.moveItemStackTo(itemStack2, this.ROWS * 9, this.slots.size(), true)) {
					if (blockEntity instanceof CompactingChestBlockEntity compactor) {
						compactor.inventoryChanged();
					}
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemStack2, 0, this.ROWS * 9, false)) {
				if (blockEntity instanceof CompactingChestBlockEntity compactor) {
					compactor.inventoryChanged();
				}
				return ItemStack.EMPTY;
			}
			
			if (itemStack2.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		
		if (blockEntity instanceof CompactingChestBlockEntity compactor) {
			compactor.inventoryChanged();
		}
		return itemStack;
	}
	
	public void toggleMode() {
		this.propertyDelegate.set(0, getCraftingMode().next().ordinal());
		broadcastChanges();
	}
	
	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		ClientPlayNetworking.send(new ChangeCompactingChestSettingsPayload(getCraftingMode()));
	}
	
	@Override
	public void removed(Player player) {
		super.removed(player);
		this.blockEntity.stopOpen(player);
	}
	
	public Container getInventory() {
		return blockEntity;
	}
	
	public CompactingChestBlockEntity getBlockEntity() {
		return blockEntity;
	}
	
	public AutoCraftingMode getCraftingMode() {
		return AutoCraftingMode.values()[propertyDelegate.get(0)];
	}
	
}
