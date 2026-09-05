package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.inventories.slots.*;
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

public class BaseInkScreenHandler extends AbstractContainerMenu implements InkColorSelectedPacketReceiver {
	
	public record ScreenOpeningData(BlockPos pos, Optional<Holder<InkColor>> inkColor) {
		public static final StreamCodec<RegistryFriendlyByteBuf, ScreenOpeningData> STREAM_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC, ScreenOpeningData::pos,
				ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(SpectrumRegistryKeys.INK_COLOR)), ScreenOpeningData::inkColor,
				ScreenOpeningData::new
		);
	}
	
	public static final int PLAYER_INVENTORY_START_X = 8;
	public static final int PLAYER_INVENTORY_START_Y = 84;
	
	protected final Level world;
	public final @Nullable ServerPlayer serverPlayer;
	protected BaseInkBlockEntity<?> blockEntity;
	protected int inventorySize;
	
	// clientside
	public BaseInkScreenHandler(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		this(SpectrumMenuTypes.INK_STORAGE, syncId, playerInventory, ScreenOpeningData.STREAM_CODEC.decode(buf), 1);
	}
	
	protected BaseInkScreenHandler(@Nullable MenuType<?> menuType, int syncId, Inventory playerInventory, ScreenOpeningData screenOpeningData, int inventorySize) {
		this(menuType, syncId, playerInventory, (BaseInkBlockEntity<?>) playerInventory.player.level().getBlockEntity(screenOpeningData.pos()), screenOpeningData.inkColor, inventorySize);
	}
	
	// serverside
	public BaseInkScreenHandler(int syncId, Inventory playerInventory, BaseInkBlockEntity<?> blockEntity, Optional<Holder<InkColor>> selectedColor) {
		this(SpectrumMenuTypes.INK_STORAGE, syncId, playerInventory, blockEntity, selectedColor, 1);
	}
	
	protected BaseInkScreenHandler(@Nullable MenuType<?> menuType, int syncId, Inventory playerInventory, BaseInkBlockEntity<?> blockEntity, Optional<Holder<InkColor>> selectedColor, int inventorySize) {
		super(menuType, syncId);
		
		this.inventorySize = inventorySize;
		this.blockEntity = blockEntity;
		this.blockEntity.setSelectedColor(selectedColor);
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
	
	public BaseInkBlockEntity<?> getBlockEntity() {
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
			ItemStack slotStack = slot.getItem();
			itemStack = slotStack.copy();
			if (index < inventorySize) {
				if (!this.moveItemStackTo(slotStack, inventorySize, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(slotStack, 0, inventorySize, false)) {
				return ItemStack.EMPTY;
			}
			
			if (slotStack.isEmpty()) {
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
	
	@Override
	public void onInkColorSelectedPacket(Optional<Holder<InkColor>> inkColor) {
		this.blockEntity.setSelectedColor(inkColor);
	}
	
}
