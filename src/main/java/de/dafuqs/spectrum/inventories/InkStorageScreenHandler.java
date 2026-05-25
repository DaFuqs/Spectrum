package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import javax.annotation.*;

public class InkStorageScreenHandler extends AbstractContainerMenu {
	
	public static final int PLAYER_INVENTORY_START_X = 8;
	public static final int PLAYER_INVENTORY_START_Y = 84;
	
	protected final Level world;
	public final ServerPlayer serverPlayer;
	protected BaseInkTransferBlockEntity<?> blockEntity;
	protected int inventorySize;
	
	// clientside
	public InkStorageScreenHandler(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		this(SpectrumMenuTypes.INK_STORAGE, syncId, playerInventory, BlockPos.STREAM_CODEC.decode(buf), 1);
	}
	
	// serverside
	public InkStorageScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
		this(SpectrumMenuTypes.INK_STORAGE, syncId, playerInventory, pos, 1);
	}
	
	protected InkStorageScreenHandler(@Nullable MenuType<?> menuType, int syncId, Inventory playerInventory, BlockPos pos, int inventorySize) {
		this(menuType, syncId, playerInventory, (BaseInkTransferBlockEntity<?>) playerInventory.player.level().getBlockEntity(pos), inventorySize);
	}
	
	protected InkStorageScreenHandler(@Nullable MenuType<?> menuType, int syncId, Inventory playerInventory, BaseInkTransferBlockEntity<?> blockEntity, int inventorySize) {
		super(menuType, syncId);
		
		this.blockEntity = blockEntity;
		this.serverPlayer = playerInventory.player instanceof ServerPlayer serverPlayerEntity ? serverPlayerEntity : null;
		this.world = playerInventory.player.level();
		
		checkContainerSize(blockEntity, inventorySize);
		blockEntity.startOpen(playerInventory.player);
		
		addBlockEntitySlots();
		
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
		
		if (this.serverPlayer != null) {
			UpdateBlockEntityInkPayload.updateBlockEntityInk(blockEntity.getBlockPos(), this.blockEntity.getInkStorage(), serverPlayer);
		}
	}
	
	public void addBlockEntitySlots() {
		this.addSlot(new InkStorageSlot(blockEntity, 0, 133, 33));
	}
	
	public BaseInkTransferBlockEntity<?> getBlockEntity() {
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
			if (index < inventorySize) {
				if (!this.moveItemStackTo(itemStack2, inventorySize, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemStack2, 0, inventorySize, false)) {
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
	
	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		
		if (this.serverPlayer != null && this.blockEntity.getInkDirty()) {
			UpdateBlockEntityInkPayload.updateBlockEntityInk(blockEntity.getBlockPos(), blockEntity.getInkStorage(), serverPlayer);
		}
	}
	
}
