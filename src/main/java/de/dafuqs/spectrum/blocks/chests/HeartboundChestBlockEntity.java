package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class HeartboundChestBlockEntity extends SpectrumChestBlockEntity implements WorldlyContainer, PlayerOwnedWithName {
	
	private UUID ownerUUID;
	private String ownerName;
	private long lastNonOwnerOpenedTick;
	
	public HeartboundChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.HEARTBOUND_CHEST.get(), blockPos, blockState);
		this.lastNonOwnerOpenedTick = -1;
	}
	
	public static int getPlayersLookingInChestCount(BlockGetter world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof HeartboundChestBlockEntity heartboundChestBlockEntity) {
				return heartboundChestBlockEntity.stateManager.getOpenerCount();
			}
		}
		return 0;
	}
	
	@Override
	protected void onInvOpenOrClose(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
		super.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
		
		if (oldViewerCount != newViewerCount) {
			updateRedstone(pos, state);
		}
	}
	
	public void updateRedstone(BlockPos pos, BlockState state) {
		if (level == null)
			return;
		
		level.updateNeighborsAt(pos, state.getBlock());
		level.updateNeighborsAt(pos.below(), state.getBlock());
		
		if (wasRecentlyTriedToOpenByNonOwner()) {
			level.scheduleTick(pos, state.getBlock(), 10);
		}
	}
	
	@Override
	protected Component getDefaultName() {
		if (hasOwner()) {
			return Component.translatable("block.spectrum.heartbound_chest.owner", this.ownerName);
		} else {
			return Component.translatable("block.spectrum.heartbound_chest");
		}
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x6(syncId, playerInventory, this, ScreenBackgroundVariant.EARLYGAME);
	}
	
	@Override
	public int getContainerSize() {
		return 54;
	}
	
	@Override
	public void onScheduledTick() {
		super.onScheduledTick();
		if (level != null)
			this.updateRedstone(this.getBlockPos(), level.getBlockState(worldPosition));
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		
		if (tag.contains("OwnerUUID")) {
			this.ownerUUID = tag.getUUID("OwnerUUID");
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
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		
		if (this.ownerUUID != null) {
			tag.putUUID("OwnerUUID", this.ownerUUID);
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
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		this.ownerName = playerEntity.getName().getString();
		setChanged();
	}
	
	@Override
	public boolean canOpen(Player player) {
		Level level = this.getLevel();
		boolean isOwnerOrSameTeamAsOwner = isOwnerOrSameTeamAsOwner(player);
		
		if (level != null && !isOwnerOrSameTeamAsOwner) {
			this.lastNonOwnerOpenedTick = this.getLevel().getGameTime();
			updateRedstone(this.worldPosition, this.getLevel().getBlockState(worldPosition));
			player.displayClientMessage(Component.translatable("block.spectrum.heartbound_chest.owner", this.ownerName), true);
		}
		
		return isOwnerOrSameTeamAsOwner;
	}
	
	public boolean wasRecentlyTriedToOpenByNonOwner() {
		if (this.getLevel() != null) {
			return this.lastNonOwnerOpenedTick > 0 && this.lastNonOwnerOpenedTick + 20 > this.getLevel().getGameTime();
		}
		return false;
	}
	
	@Override
	public int[] getSlotsForFace(Direction side) {
		return new int[0];
	}
	
	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
		return false;
	}
	
	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
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
