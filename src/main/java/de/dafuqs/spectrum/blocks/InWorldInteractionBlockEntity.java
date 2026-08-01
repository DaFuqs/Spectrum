package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.api.block.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import org.jspecify.annotations.*;

public abstract class InWorldInteractionBlockEntity extends BlockEntity implements RandomizableContainer, ImplementedInventory {
	
	protected NonNullList<ItemStack> items;
	@Nullable
	protected ResourceKey<LootTable> lootTable;
	protected long lootTableSeed;
	
	public InWorldInteractionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
		super(type, pos, state);
		this.items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
	}
	
	// interaction methods
	public void updateInClientWorld() {
		if (level != null) {
			level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), Block.UPDATE_INVISIBLE);
		}
	}
	
	// Called when the chunk is first loaded to initialize this be
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		CompoundTag nbtCompound = new CompoundTag();
		this.saveAdditional(nbtCompound, registryLookup);
		return nbtCompound;
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(tag)) {
			ContainerHelper.loadAllItems(tag, this.items, registryLookup);
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		if (!this.trySaveLootTable(tag)) {
			ContainerHelper.saveAllItems(tag, this.items, registryLookup);
		}
	}
	
	@Override
	public void unpackLootTable(@Nullable Player player) {
		if (lootTableSeed == 0 && level != null) {
			lootTableSeed = level.getRandom().nextLong();
		}
		RandomizableContainer.super.unpackLootTable(player);
	}

	@Override
	public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public NonNullList<ItemStack> getItems() {
		return items;
	}
	
	@Override
	public void inventoryChanged() {
		this.setChanged();
	}
	
	@Override
	public void setChanged() {
		super.setChanged();
		if (level instanceof ServerLevel serverWorld) {
			serverWorld.getChunkSource().blockChanged(worldPosition);
		}
	}
	
	@Override
	public @Nullable ResourceKey<LootTable> getLootTable() {
		return lootTable;
	}
	
	@Override
	public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
		this.lootTable = lootTable;
	}
	
	@Override
	public long getLootTableSeed() {
		return lootTableSeed;
	}
	
	@Override
	public void setLootTableSeed(long lootTableSeed) {
		this.lootTableSeed = lootTableSeed;
	}
	
}
