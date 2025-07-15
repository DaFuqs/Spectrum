package de.dafuqs.spectrum.inventories;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.blocks.potion_workshop.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class PotionWorkshopScreenHandler extends AbstractContainerMenu {
	
	protected final Level world;
	private final Container inventory;
	private final ContainerData propertyDelegate;
	
	public PotionWorkshopScreenHandler(int syncId, Inventory playerInventory) {
		this(SpectrumScreenHandlerTypes.POTION_WORKSHOP, syncId, playerInventory);
	}
	
	public PotionWorkshopScreenHandler(int syncId, Inventory playerInventory, PotionWorkshopBlockEntity potionWorkshopBlockEntity, ContainerData propertyDelegate) {
		this(SpectrumScreenHandlerTypes.POTION_WORKSHOP, syncId, playerInventory, potionWorkshopBlockEntity, propertyDelegate);
	}
	
	public PotionWorkshopScreenHandler(MenuType<?> type, int i, Inventory playerInventory) {
		this(type, i, playerInventory, new SimpleContainer(PotionWorkshopBlockEntity.INVENTORY_SIZE), new SimpleContainerData(3));
	}
	
	protected PotionWorkshopScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
		super(type, syncId);
		this.inventory = inventory;
		this.world = playerInventory.player.level();
		
		checkContainerDataCount(propertyDelegate, 3);
		this.propertyDelegate = propertyDelegate;
		this.addDataSlots(propertyDelegate);
		
		checkContainerSize(inventory, PotionWorkshopBlockEntity.INVENTORY_SIZE);
		inventory.startOpen(playerInventory.player);
		
		// mermaids gem slot
		this.addSlot(new StackFilterSlot(inventory, 0, 26, 85, SpectrumItems.MERMAIDS_GEM));
		
		// base ingredient inventory
		this.addSlot(new Slot(inventory, 1, 134, 41));
		
		// ingredient slots
		this.addSlot(new Slot(inventory, 2, 26, 23));
		this.addSlot(new Slot(inventory, 3, 11, 42));
		this.addSlot(new Slot(inventory, 4, 41, 42));
		
		// reagent slots
		if (AdvancementHelper.hasAdvancement(playerInventory.player, SpectrumAdvancements.FOURTH_BREWING_SLOT)) {
			this.addSlot(new ReagentSlot(inventory, 5, 51, 19));
			this.addSlot(new ReagentSlot(inventory, 6, 74, 19));
			this.addSlot(new ReagentSlot(inventory, 7, 97, 19));
			this.addSlot(new ReagentSlot(inventory, 8, 120, 19));
		} else {
			this.addSlot(new ReagentSlot(inventory, 5, 58, 19));
			this.addSlot(new ReagentSlot(inventory, 6, 85, 19));
			this.addSlot(new ReagentSlot(inventory, 7, 112, 19));
			this.addSlot(new DisabledSlot(inventory, 8, -2000, 19));
		}
		
		// output inventory
		for (int j = 0; j < 2; ++j) {
			for (int k = 0; k < 6; ++k) {
				this.addSlot(new Slot(inventory, 9 + k + j * 6, 62 + k * 18, 67 + j * 18));
			}
		}
		
		// player inventory
		for (int j = 0; j < 3; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 120 + j * 18));
			}
		}
		
		// player hotbar
		for (int j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 178));
		}
	}
	
	@Override
	public boolean stillValid(Player player) {
		return this.inventory.stillValid(player);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack slotStackCopy = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		
		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			slotStackCopy = slotStack.copy();
			if (index < PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT) {
				// workshop (not output inv)
				if (!this.moveItemStackTo(slotStack, PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT + 12, this.slots.size(), false)) {
					if (inventory instanceof PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
						potionWorkshopBlockEntity.inventoryChanged();
					}
					return ItemStack.EMPTY;
				}
			} else if (index < PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT + 12) {
				// workshop (output inv)
				if (!this.moveItemStackTo(slotStack, PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT + 12, this.slots.size(), false)) {
					return ItemStack.EMPTY;
				}
				// from player inv
				// is reagent?
			} else if (!this.moveItemStackTo(slotStack, PotionWorkshopBlockEntity.FIRST_REAGENT_SLOT, PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT, false)) {
				if (!slotStack.isEmpty()) {
					this.moveItemStackTo(slotStack, 0, PotionWorkshopBlockEntity.FIRST_REAGENT_SLOT, false);
				}
				if (inventory instanceof PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
					potionWorkshopBlockEntity.inventoryChanged();
				}
				return ItemStack.EMPTY;
				// others
			} else if (slotStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		
		if (inventory instanceof PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
			potionWorkshopBlockEntity.inventoryChanged();
		}
		return slotStackCopy;
	}
	
	public Container getInventory() {
		return this.inventory;
	}
	
	@Override
	public void removed(Player player) {
		super.removed(player);
		this.inventory.stopOpen(player);
	}
	
	public int getBrewTime() {
		return this.propertyDelegate.get(0);
	}
	
	public int getMaxBrewTime() {
		return this.propertyDelegate.get(1);
	}
	
	public int getPotionColor() {
		return this.propertyDelegate.get(2);
	}
}
