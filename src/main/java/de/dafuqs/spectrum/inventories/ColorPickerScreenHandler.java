package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.energy.ColorPickerBlockEntity;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlockEntity;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.inventories.slots.DisabledSlot;
import de.dafuqs.spectrum.inventories.slots.ReagentSlot;
import de.dafuqs.spectrum.inventories.slots.StackFilterSlot;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class ColorPickerScreenHandler extends ScreenHandler {
	
	private final Inventory inventory;
	protected final World world;
	private final PropertyDelegate propertyDelegate;
	
	public ColorPickerScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(SpectrumScreenHandlerTypes.COLOR_PICKER, syncId, playerInventory);
	}
	
	public ColorPickerScreenHandler(int syncId, PlayerInventory playerInventory, ColorPickerBlockEntity colorPickerBlockEntity, PropertyDelegate propertyDelegate) {
		this(SpectrumScreenHandlerTypes.COLOR_PICKER, syncId, playerInventory, colorPickerBlockEntity, propertyDelegate);
	}

	public ColorPickerScreenHandler(ScreenHandlerType<?> type, int i, PlayerInventory playerInventory) {
		this(type, i, playerInventory, new SimpleInventory(PotionWorkshopBlockEntity.INVENTORY_SIZE), new ArrayPropertyDelegate(3));
	}

	protected ColorPickerScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(type, syncId);
		this.inventory = inventory;
		this.world = playerInventory.player.world;
		
		checkDataCount(propertyDelegate, 3);
		this.propertyDelegate = propertyDelegate;
		this.addProperties(propertyDelegate);

		checkSize(inventory, PotionWorkshopBlockEntity.INVENTORY_SIZE);
		inventory.onOpen(playerInventory.player);
		
		// ingredient slots
		this.addSlot(new Slot(inventory, 2, 26, 23));
		this.addSlot(new Slot(inventory, 3, 11, 42));
		this.addSlot(new Slot(inventory, 4, 41, 42));
		
		// player inventory
		for(int j = 0; j < 3; ++j) {
			for(int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 120 + j * 18));
			}
		}
		
		// player hotbar
		for(int j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 178));
		}
	}
	
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack slotStackCopy = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		
		if (slot.hasStack()) {
			ItemStack slotStack = slot.getStack();
			slotStackCopy = slotStack.copy();
			if(index < PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT) {
				// workshop (not output inv)
				if (!this.insertItem(slotStack, PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT + 12, this.slots.size(), false)) {
					if (inventory instanceof PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
						potionWorkshopBlockEntity.inventoryChanged();
					}
					return ItemStack.EMPTY;
				}
			} else if(index < PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT + 12) {
				// workshop (output inv)
				if (!this.insertItem(slotStack, PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT + 12, this.slots.size(), false)) {
					return ItemStack.EMPTY;
				}
			// from player inv
			// is reagent?
			} else if(!this.insertItem(slotStack, PotionWorkshopBlockEntity.FIRST_REAGENT_SLOT, PotionWorkshopBlockEntity.FIRST_INVENTORY_SLOT, false)) {
				if(!slotStack.isEmpty()) {
					this.insertItem(slotStack, 0, PotionWorkshopBlockEntity.FIRST_REAGENT_SLOT, false);
				}
				if(inventory instanceof PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
					potionWorkshopBlockEntity.inventoryChanged();
				}
				return ItemStack.EMPTY;
			// others
			} else if (slotStack.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
		}
		
		if(inventory instanceof PotionWorkshopBlockEntity potionWorkshopBlockEntity) {
			potionWorkshopBlockEntity.inventoryChanged();
		}
		return slotStackCopy;
	}

	public Inventory getInventory() {
		return this.inventory;
	}
	
	public void close(PlayerEntity player) {
		super.close(player);
		this.inventory.onClose(player);
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
