package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.screen.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class HeartboundChestBlockEntity extends SpectrumChestBlockEntity implements SidedInventory, PlayerOwnedWithName {
	
	private UUID ownerUUID;
	private String ownerName;
	private long lastNonOwnerOpenedTick;
	
	public HeartboundChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.HEARTBOUND_CHEST, blockPos, blockState);
		this.lastNonOwnerOpenedTick = -1;
	}
	
	public static int getPlayersLookingInChestCount(BlockView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof HeartboundChestBlockEntity heartboundChestBlockEntity) {
				return heartboundChestBlockEntity.stateManager.getViewerCount();
			}
		}
		return 0;
	}
	
	@Override
	protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
		super.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
		
		if (oldViewerCount != newViewerCount) {
			updateRedstone(pos, state);
		}
	}
	
	public void updateRedstone(BlockPos pos, BlockState state) {
		world.updateNeighborsAlways(pos, state.getBlock());
		world.updateNeighborsAlways(pos.down(), state.getBlock());
		
		if (wasRecentlyTriedToOpenByNonOwner()) {
			world.scheduleBlockTick(pos, state.getBlock(), 10);
		}
	}
	
	@Override
	protected Text getContainerName() {
		if (hasOwner()) {
			return Text.translatable("block.spectrum.heartbound_chest.owner", this.ownerName);
		} else {
			return Text.translatable("block.spectrum.heartbound_chest");
		}
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x6(syncId, playerInventory, this, ScreenBackgroundVariant.EARLYGAME);
	}
	
	@Override
	public int size() {
		return 54;
	}
	
	@Override
	public void onScheduledTick() {
		super.onScheduledTick();
		this.updateRedstone(this.getPos(), this.getWorld().getBlockState(pos));
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		
		if (tag.contains("OwnerUUID")) {
			this.ownerUUID = tag.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (tag.contains("OwnerName")) {
			this.ownerName = tag.getString("OwnerName");
		} else {
			this.ownerName = "???";
		}
		
		if (tag.contains("LastNonOwnerOpenedTick")) {
			this.lastNonOwnerOpenedTick = tag.getLong("LastNonOwnerOpenedTick");
		} else {
			this.lastNonOwnerOpenedTick = -1;
		}
	}
	
	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		
		if (this.ownerUUID != null) {
			tag.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.ownerName != null) {
			tag.putString("OwnerName", this.ownerName);
		}
		
		tag.putLong("LastNonOwnerOpenedTick", this.lastNonOwnerOpenedTick);
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public String getOwnerName() {
		return this.ownerName;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		this.ownerName = playerEntity.getName().getString();
		markDirty();
	}
	
	@Override
	public boolean checkUnlocked(PlayerEntity player) {
		boolean isOwner = this.getOwnerUUID().equals(player.getUuid());
		
		if (!isOwner && this.getWorld() != null) {
			this.lastNonOwnerOpenedTick = this.getWorld().getTime();
			updateRedstone(this.pos, this.getWorld().getBlockState(pos));
			player.sendMessage(Text.translatable("block.spectrum.heartbound_chest.owner", this.ownerName), true);
		}
		
		return isOwner;
	}
	
	public boolean wasRecentlyTriedToOpenByNonOwner() {
		if (this.getWorld() != null) {
			return this.lastNonOwnerOpenedTick > 0 && this.lastNonOwnerOpenedTick + 20 > this.getWorld().getTime();
		}
		return false;
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		return new int[0];
	}
	
	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return false;
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}
	
	public boolean canBreak(UUID uuid) {
		if (this.ownerUUID == null) {
			return true;
		} else {
			return this.ownerUUID.equals(uuid);
		}
	}
}
