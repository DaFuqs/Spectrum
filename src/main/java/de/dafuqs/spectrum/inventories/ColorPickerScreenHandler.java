package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.energy.ColorPickerBlockEntity;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.inventories.slots.ColorPickerInputSlot;
import de.dafuqs.spectrum.inventories.slots.InkStorageSlot;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColorPickerScreenHandler extends ScreenHandler implements InkColorSelectedPacketReceiver {
	
	public static final int PLAYER_INVENTORY_START_X = 8;
	public static final int PLAYER_INVENTORY_START_Y = 84;
	
	protected final World world;
	protected ColorPickerBlockEntity blockEntity;
	
	public final ServerPlayerEntity player;
	
	@Override
	public void sendContentUpdates() {
		super.sendContentUpdates();
		
		if(this.player != null && this.blockEntity.shouldUpdateClients()) {
			SpectrumS2CPacketSender.updateBlockEntityInk(blockEntity.getPos(), blockEntity.getEnergyStorage(), player);
		}
	}
	
	public ColorPickerScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
		this(syncId, playerInventory, buf.readBlockPos());
	}
	
	public ColorPickerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos readBlockPos) {
		super(SpectrumScreenHandlerTypes.COLOR_PICKER, syncId);
		this.player = playerInventory.player instanceof ServerPlayerEntity serverPlayerEntity ? serverPlayerEntity : null;
		this.world = playerInventory.player.world;
		BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(readBlockPos);
		if (blockEntity instanceof ColorPickerBlockEntity colorPickerBlockEntity) {
			this.blockEntity = colorPickerBlockEntity;
		} else {
			throw new IllegalArgumentException("Color Picker GUI called with a position where no ColorPickerBlockEntity exists");
		}
		
		checkSize(colorPickerBlockEntity, ColorPickerBlockEntity.INVENTORY_SIZE);
		colorPickerBlockEntity.onOpen(playerInventory.player);
		
		// color picker slots
		this.addSlot(new ColorPickerInputSlot(colorPickerBlockEntity, 0, 26, 33));
		this.addSlot(new InkStorageSlot(colorPickerBlockEntity, 1, 133, 33));
		
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
		
		if(this.player != null) {
			SpectrumS2CPacketSender.updateBlockEntityInk(blockEntity.getPos(), this.blockEntity.getEnergyStorage(), player);
		}
	}
	
	public ColorPickerBlockEntity getBlockEntity() {
		return this.blockEntity;
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return this.blockEntity.canPlayerUse(player);
	}
	
	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		this.blockEntity.onClose(player);
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index < ColorPickerBlockEntity.INVENTORY_SIZE) {
				if (!this.insertItem(itemStack2, ColorPickerBlockEntity.INVENTORY_SIZE, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 0, ColorPickerBlockEntity.INVENTORY_SIZE, false)) {
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
	
	@Override
	public void onInkColorSelectedPacket(InkColor inkColor) {
		this.blockEntity.setSelectedColor(inkColor);
	}
	
}
