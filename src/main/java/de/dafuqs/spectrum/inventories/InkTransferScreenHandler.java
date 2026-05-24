package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.energy.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import javax.annotation.*;
import java.util.*;

public abstract class InkTransferScreenHandler extends AbstractContainerMenu implements InkColorSelectedPacketReceiver {
	
	public record ScreenOpeningData(BlockPos pos, Optional<Holder<InkColor>> inkColor) {
		public static final StreamCodec<RegistryFriendlyByteBuf, ScreenOpeningData> PACKET_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC, ScreenOpeningData::pos,
				ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(SpectrumRegistryKeys.INK_COLOR)), ScreenOpeningData::inkColor,
				ScreenOpeningData::new
		);
	}
	
	public static final int PLAYER_INVENTORY_START_X = 8;
	public static final int PLAYER_INVENTORY_START_Y = 84;
	
	protected final Level world;
	public final ServerPlayer serverPlayer;
	protected BaseInkTransferBlockEntity<?> blockEntity;
	
	// clientside
	protected InkTransferScreenHandler(@Nullable MenuType<?> menuType, int syncId, Inventory playerInventory, ScreenOpeningData screenOpeningData) {
		this(menuType, syncId, playerInventory, (BaseInkTransferBlockEntity<?>) playerInventory.player.level().getBlockEntity(screenOpeningData.pos()), screenOpeningData.inkColor());
	}
	
	// serverside
	protected InkTransferScreenHandler(@Nullable MenuType<?> menuType, int syncId, Inventory playerInventory, BaseInkTransferBlockEntity<?> blockEntity, Optional<Holder<InkColor>> selectedColor) {
		super(menuType, syncId);
		
		this.serverPlayer = playerInventory.player instanceof ServerPlayer serverPlayerEntity ? serverPlayerEntity : null;
		this.world = playerInventory.player.level();
		this.blockEntity = blockEntity;
		this.blockEntity.setSelectedColor(selectedColor);
		
		checkContainerSize(blockEntity, ColorPickerBlockEntity.INVENTORY_SIZE);
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
	
	public abstract void addBlockEntitySlots();
	
	@Override
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
			if (index < ColorPickerBlockEntity.INVENTORY_SIZE) {
				if (!this.moveItemStackTo(itemStack2, ColorPickerBlockEntity.INVENTORY_SIZE, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemStack2, 0, ColorPickerBlockEntity.INVENTORY_SIZE, false)) {
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
	public void onInkColorSelectedPacket(Optional<Holder<InkColor>> inkColor) {
		this.blockEntity.setSelectedColor(inkColor);
	}
	
	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		
		if (this.serverPlayer != null && this.blockEntity.getInkDirty()) {
			UpdateBlockEntityInkPayload.updateBlockEntityInk(blockEntity.getBlockPos(), blockEntity.getInkStorage(), serverPlayer);
		}
	}
	
}
