package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.cinderhearth.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.server.network.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class CinderhearthScreenHandler extends ScreenHandler {
	
	public static final int PLAYER_INVENTORY_START_X = 8;
	public static final int PLAYER_INVENTORY_START_Y = 84;
	
	protected final World world;
	protected CinderhearthBlockEntity blockEntity;
	private final PropertyDelegate propertyDelegate;
	
	public final ServerPlayerEntity player;
	
	@Override
	public void sendContentUpdates() {
		super.sendContentUpdates();
		
		if (this.player != null && this.blockEntity.getInkDirty()) {
			SpectrumS2CPacketSender.updateBlockEntityInk(blockEntity.getPos(), blockEntity.getEnergyStorage(), player);
		}
	}
	
	public CinderhearthScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
		this(syncId, playerInventory, buf.readBlockPos(), new ArrayPropertyDelegate(2));
	}
	
	public CinderhearthScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos readBlockPos, PropertyDelegate propertyDelegate) {
		super(SpectrumScreenHandlerTypes.CINDERHEARTH, syncId);
		this.player = playerInventory.player instanceof ServerPlayerEntity serverPlayerEntity ? serverPlayerEntity : null;
		this.world = playerInventory.player.world;
		this.propertyDelegate = propertyDelegate;
		BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(readBlockPos);
		if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
			this.blockEntity = cinderhearthBlockEntity;
		} else {
			throw new IllegalArgumentException("GUI called with a position where no valid BlockEntity exists");
		}
		
		checkSize(cinderhearthBlockEntity, CinderhearthBlockEntity.INVENTORY_SIZE);
		cinderhearthBlockEntity.onOpen(playerInventory.player);
		
		this.addSlot(new InkInputSlot(cinderhearthBlockEntity, CinderhearthBlockEntity.INK_PROVIDER_SLOT_ID, 146, 13));
		this.addSlot(new ExperienceStorageItemSlot(cinderhearthBlockEntity, CinderhearthBlockEntity.EXPERIENCE_STORAGE_ITEM_SLOT_ID, 38, 52));
		this.addSlot(new Slot(cinderhearthBlockEntity, CinderhearthBlockEntity.INPUT_SLOT_ID, 14, 28));
		
		for (int i = 0; i < 4; i++) {
			this.addSlot(new Slot(cinderhearthBlockEntity, CinderhearthBlockEntity.FIRST_OUTPUT_SLOT_ID + i, 62 + i * 18, 28));
		}
		for (int i = 0; i < 4; i++) {
			this.addSlot(new Slot(cinderhearthBlockEntity, CinderhearthBlockEntity.FIRST_OUTPUT_SLOT_ID + 4 + i, 62 + i * 18, 28 + 18));
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
			SpectrumS2CPacketSender.updateBlockEntityInk(blockEntity.getPos(), this.blockEntity.getEnergyStorage(), player);
		}
		
		this.addProperties(propertyDelegate);
	}
	
	public CinderhearthBlockEntity getBlockEntity() {
		return this.blockEntity;
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return this.blockEntity.canPlayerUse(player);
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		this.blockEntity.onClose(player);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index < CinderhearthBlockEntity.INVENTORY_SIZE) {
				if (!this.insertItem(itemStack2, CinderhearthBlockEntity.INVENTORY_SIZE, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 0, CinderhearthBlockEntity.INVENTORY_SIZE, false)) {
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
	
	public int getCraftingTime() {
		return this.propertyDelegate.get(0);
	}
	
	public int getCraftingTimeTotal() {
		return this.propertyDelegate.get(1);
	}
	
}
