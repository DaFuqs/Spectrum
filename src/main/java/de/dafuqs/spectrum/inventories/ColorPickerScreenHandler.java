package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.energy.ColorPickerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColorPickerScreenHandler extends ScreenHandler {
	
	public static final int PLAYER_INVENTORY_START_X = 8;
	public static final int PLAYER_INVENTORY_START_Y = 84;
	
	protected final World world;
	protected ColorPickerBlockEntity blockEntity;
	
	public ColorPickerScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
		this(syncId, playerInventory, buf.readBlockPos());
	}

	public ColorPickerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos readBlockPos) {
		super(SpectrumScreenHandlerTypes.COLOR_PICKER, syncId);
		this.world = playerInventory.player.world;
		BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(readBlockPos);
		if(blockEntity instanceof ColorPickerBlockEntity colorPickerBlockEntity) {
			this.blockEntity = colorPickerBlockEntity;
		} else {
			throw new IllegalArgumentException("Color Picker GUI called with a position where no ColorPickerBlockEntity exists");
		}
		
		checkSize(colorPickerBlockEntity, ColorPickerBlockEntity.INVENTORY_SIZE);
		colorPickerBlockEntity.onOpen(playerInventory.player);
		
		// color picker slots
		this.addSlot(new Slot(colorPickerBlockEntity, 0, 26, 33));
		this.addSlot(new Slot(colorPickerBlockEntity, 1, 133, 33));
		
		// player inventory
		for(int j = 0; j < 3; ++j) {
			for(int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, PLAYER_INVENTORY_START_X + k * 18, PLAYER_INVENTORY_START_Y + j * 18));
			}
		}
		
		// player hotbar
		for(int j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, PLAYER_INVENTORY_START_X + j * 18, PLAYER_INVENTORY_START_Y + 58));
		}
	}
	
	public ColorPickerBlockEntity getBlockEntity() {
		return this.blockEntity;
	}
	
	public boolean canUse(PlayerEntity player) {
		return this.blockEntity.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}
	
	public void close(PlayerEntity player) {
		super.close(player);
		this.blockEntity.onClose(player);
	}
	
}
