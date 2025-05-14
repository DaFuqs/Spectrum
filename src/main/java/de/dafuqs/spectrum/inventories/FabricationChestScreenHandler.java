package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class FabricationChestScreenHandler extends AbstractContainerMenu {
	
	protected final Level world;
	private final Container inventory;
	
	public FabricationChestScreenHandler(int syncId, Inventory playerInventory) {
		this(SpectrumScreenHandlerTypes.FABRICATION_CHEST, syncId, playerInventory);
	}
	
	protected FabricationChestScreenHandler(MenuType<?> type, int i, Inventory playerInventory) {
		this(type, i, playerInventory, new SimpleContainer(FabricationChestBlockEntity.INVENTORY_SIZE));
	}
	
	public FabricationChestScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
		this(SpectrumScreenHandlerTypes.FABRICATION_CHEST, syncId, playerInventory, inventory);
	}
	
	protected FabricationChestScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, Container inventory) {
		super(type, syncId);
		this.inventory = inventory;
		this.world = playerInventory.player.level();
		
		checkContainerSize(inventory, FabricationChestBlockEntity.INVENTORY_SIZE);
		inventory.startOpen(playerInventory.player);
		
		// chest inventory
		int l;
		for (l = 0; l < 3; ++l) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventory, l * 9 + k, 8 + k * 18, 67 + l * 18));
			}
		}
		
		// crafting tablet slots
		for (int j = 0; j < 4; j++) {
			int slotId = FabricationChestBlockEntity.RECIPE_SLOTS[j];
			this.addSlot(new StackFilterSlot(inventory, slotId, 26 + j * 36, 18, SpectrumItems.CRAFTING_TABLET));
		}
		
		// crafting result slots
		for (int j = 0; j < 4; j++) {
			int slotId = FabricationChestBlockEntity.RESULT_SLOTS[j];
			this.addSlot(new ExtractOnlySlot(inventory, slotId, 26 + j * 36, 42));
		}
		
		// player inventory
		for (l = 0; l < 3; ++l) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, 138 + l * 18));
			}
		}
		
		// player hotbar
		for (l = 0; l < 9; ++l) {
			this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 196));
		}
	}
	
	@Override
	public boolean stillValid(Player player) {
		return this.inventory.stillValid(player);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack clickedStackCopy = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		
		if (slot.hasItem()) {
			ItemStack clickedStack = slot.getItem();
			clickedStackCopy = clickedStack.copy();
			
			if (index < FabricationChestBlockEntity.INVENTORY_SIZE) {
				// => player inv
				if (!this.moveItemStackTo(clickedStack, 35, 71, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index > FabricationChestBlockEntity.INVENTORY_SIZE && clickedStackCopy.is(SpectrumItems.CRAFTING_TABLET)) {
				if (!this.moveItemStackTo(clickedStack, FabricationChestBlockEntity.RECIPE_SLOTS[0], FabricationChestBlockEntity.RECIPE_SLOTS[FabricationChestBlockEntity.RECIPE_SLOTS.length - 1] + 1, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			// chest => inventory
			if (!this.moveItemStackTo(clickedStack, 0, FabricationChestBlockEntity.CHEST_SLOTS.length - 1, false)) {
				return ItemStack.EMPTY;
			}
			
			if (clickedStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			
			if (clickedStack.getCount() == clickedStackCopy.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, clickedStack);
		}
		
		
		return clickedStackCopy;
	}
	
	public Container getInventory() {
		return this.inventory;
	}
	
	@Override
	public void removed(Player player) {
		super.removed(player);
		this.inventory.stopOpen(player);
	}
	
}
