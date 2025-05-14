package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.cinderhearth.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class CinderhearthScreenHandler extends AbstractContainerMenu {
	
	public static final int PLAYER_INVENTORY_START_X = 8;
	public static final int PLAYER_INVENTORY_START_Y = 84;
	
	protected final Level world;
	private final CinderhearthBlockEntity blockEntity;
	private final ContainerData propertyDelegate;
	
	public final ServerPlayer player;
	
	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		
		if (this.player != null && this.blockEntity.getInkDirty()) {
			UpdateBlockEntityInkPayload.updateBlockEntityInk(blockEntity.getBlockPos(), blockEntity.getEnergyStorage(), player);
		}
	}
	
	public CinderhearthScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
		this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(pos, SpectrumBlockEntities.CINDERHEARTH).orElseThrow(), new SimpleContainerData(2));
	}
	
	public CinderhearthScreenHandler(int syncId, Inventory playerInventory, CinderhearthBlockEntity blockEntity, ContainerData propertyDelegate) {
		super(SpectrumScreenHandlerTypes.CINDERHEARTH, syncId);
		
		this.player = playerInventory.player instanceof ServerPlayer serverPlayerEntity ? serverPlayerEntity : null;
		this.world = playerInventory.player.level();
		this.propertyDelegate = propertyDelegate;
		this.blockEntity = blockEntity;
		
		checkContainerSize(blockEntity, CinderhearthBlockEntity.INVENTORY_SIZE);
		blockEntity.startOpen(playerInventory.player);
		
		this.addSlot(new InkInputSlot(blockEntity, CinderhearthBlockEntity.INK_PROVIDER_SLOT_ID, 146, 13));
		this.addSlot(new ExperienceStorageItemSlot(blockEntity, CinderhearthBlockEntity.EXPERIENCE_STORAGE_ITEM_SLOT_ID, 38, 52));
		this.addSlot(new Slot(blockEntity, CinderhearthBlockEntity.INPUT_SLOT_ID, 14, 28));
		
		for (int i = 0; i < 4; i++) {
			this.addSlot(new Slot(blockEntity, CinderhearthBlockEntity.FIRST_OUTPUT_SLOT_ID + i, 62 + i * 18, 28));
		}
		for (int i = 0; i < 4; i++) {
			this.addSlot(new Slot(blockEntity, CinderhearthBlockEntity.FIRST_OUTPUT_SLOT_ID + 4 + i, 62 + i * 18, 28 + 18));
		}
		
		// player inventory
		for (int j = 0; j < 3; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, PLAYER_INVENTORY_START_X + k * 18, PLAYER_INVENTORY_START_Y + j * 18));
			}
		}
		
		// player hotbar
		for (int j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, PLAYER_INVENTORY_START_X + j * 18, PLAYER_INVENTORY_START_Y + 58));
		}
		
		if (this.player != null) {
			UpdateBlockEntityInkPayload.updateBlockEntityInk(blockEntity.getBlockPos(), this.blockEntity.getEnergyStorage(), player);
		}
		
		this.addDataSlots(propertyDelegate);
	}
	
	public CinderhearthBlockEntity getBlockEntity() {
		return this.blockEntity;
	}
	
	@Override
	public boolean stillValid(Player player) {
		return this.blockEntity.stillValid(player);
	}
	
	@Override
	public void removed(Player player) {
		super.removed(player);
		this.blockEntity.stopOpen(player);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();
			if (index < CinderhearthBlockEntity.INVENTORY_SIZE) {
				if (!this.moveItemStackTo(itemStack2, CinderhearthBlockEntity.INVENTORY_SIZE, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemStack2, 0, CinderhearthBlockEntity.INVENTORY_SIZE, false)) {
				return ItemStack.EMPTY;
			}
			
			if (itemStack2.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		
		return itemStack;
	}
	
	public int getCraftingTime() {
		return this.propertyDelegate.get(0);
	}
	
	public int getCraftingTimeTotal() {
		return this.propertyDelegate.get(1);
	}
	
}
