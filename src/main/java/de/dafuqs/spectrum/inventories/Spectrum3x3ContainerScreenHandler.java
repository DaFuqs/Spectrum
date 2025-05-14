package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.redstone.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public class Spectrum3x3ContainerScreenHandler extends AbstractContainerMenu {
	
	private final ScreenBackgroundVariant tier;
	private final Container inventory;
	
	public Spectrum3x3ContainerScreenHandler(int syncId, Inventory playerInventory, ScreenBackgroundVariant screenBackgroundVariant) {
		this(SpectrumScreenHandlerTypes.GENERIC_TIER1_3X3, syncId, playerInventory, new SimpleContainer(9), screenBackgroundVariant);
	}
	
	public Spectrum3x3ContainerScreenHandler(MenuType<Spectrum3x3ContainerScreenHandler> screenHandlerType, int syncId, Inventory playerInventory, Container inventory, ScreenBackgroundVariant screenBackgroundVariant) {
		super(screenHandlerType, syncId);
		checkContainerSize(inventory, 9);
		this.tier = screenBackgroundVariant;
		this.inventory = inventory;
		inventory.startOpen(playerInventory.player);
		
		int m;
		int l;
		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 3; ++l) {
				this.addSlot(new Slot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18));
			}
		}
		
		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}
		
		for (m = 0; m < 9; ++m) {
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
		}
	}
	
	@Contract("_, _ -> new")
	public static @NotNull Spectrum3x3ContainerScreenHandler createTier1(int syncId, Inventory playerInventory) {
		return new Spectrum3x3ContainerScreenHandler(syncId, playerInventory, ScreenBackgroundVariant.EARLYGAME);
	}
	
	@Contract("_, _, _ -> new")
	public static @NotNull AbstractContainerMenu createTier1(int syncId, Inventory playerInventory, BlockPlacerBlockEntity blockEntity) {
		return new Spectrum3x3ContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_3X3, syncId, playerInventory, blockEntity, ScreenBackgroundVariant.EARLYGAME);
	}
	
	@Contract("_, _ -> new")
	public static @NotNull Spectrum3x3ContainerScreenHandler createTier2(int syncId, Inventory playerInventory) {
		return new Spectrum3x3ContainerScreenHandler(syncId, playerInventory, ScreenBackgroundVariant.MIDGAME);
	}
	
	@Contract("_, _, _ -> new")
	public static @NotNull AbstractContainerMenu createTier2(int syncId, Inventory playerInventory, BlockPlacerBlockEntity blockEntity) {
		return new Spectrum3x3ContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_3X3, syncId, playerInventory, blockEntity, ScreenBackgroundVariant.MIDGAME);
	}
	
	@Contract("_, _ -> new")
	public static @NotNull Spectrum3x3ContainerScreenHandler createTier3(int syncId, Inventory playerInventory) {
		return new Spectrum3x3ContainerScreenHandler(syncId, playerInventory, ScreenBackgroundVariant.LATEGAME);
	}
	
	@Contract("_, _, _ -> new")
	public static @NotNull AbstractContainerMenu createTier3(int syncId, Inventory playerInventory, BlockPlacerBlockEntity blockEntity) {
		return new Spectrum3x3ContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_3X3, syncId, playerInventory, blockEntity, ScreenBackgroundVariant.LATEGAME);
	}
	
	@Override
	public boolean stillValid(Player player) {
		return this.inventory.stillValid(player);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();
			if (index < 9) {
				if (!this.moveItemStackTo(itemStack2, 9, 45, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemStack2, 0, 9, false)) {
				return ItemStack.EMPTY;
			}
			
			if (itemStack2.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			
			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, itemStack2);
		}
		
		return itemStack;
	}
	
	@Override
	public void removed(Player player) {
		super.removed(player);
		this.inventory.stopOpen(player);
	}
	
	public ScreenBackgroundVariant getTier() {
		return this.tier;
	}
	
}
