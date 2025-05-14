package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.api.block.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import org.jetbrains.annotations.*;

public abstract class InWorldInteractionBlockEntity extends BlockEntity implements RandomizableContainer, ImplementedInventory {
	
	private final int inventorySize;
	protected NonNullList<ItemStack> items;
	@Nullable
	protected ResourceKey<LootTable> lootTable;
	protected long lootTableSeed;
	
	public InWorldInteractionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
		super(type, pos, state);
		this.inventorySize = inventorySize;
		this.items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
	}
	
	// interaction methods
	public void updateInClientWorld() {
		if (level instanceof ServerLevel serverWorld)
			serverWorld.getChunkSource().blockChanged(worldPosition);
	}
	
	// Called when the chunk is first loaded to initialize this be
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		CompoundTag nbtCompound = new CompoundTag();
		this.saveAdditional(nbtCompound, registryLookup);
		return nbtCompound;
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
		if (!this.tryLoadLootTable(nbt)) {
			ContainerHelper.loadAllItems(nbt, items, registryLookup);
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		if (!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, items, registryLookup);
		}
	}

	@Override
	public void unpackLootTable(@Nullable Player player) {
		if (lootTableSeed == 0 && level != null)
			lootTableSeed = level.getRandom().nextLong();
		RandomizableContainer.super.unpackLootTable(player);
		this.setChanged();
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public NonNullList<ItemStack> getItems() {
		return items;
	}
	
	@Override
	public void inventoryChanged() {
		this.setChanged();
		if (level != null && !level.isClientSide) {
			updateInClientWorld();
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
	
	@Override
	protected void applyImplicitComponents(BlockEntity.DataComponentInput components) {
		super.applyImplicitComponents(components);
		SeededContainerLoot containerLootComponent = components.get(DataComponents.CONTAINER_LOOT);
		if (containerLootComponent != null) {
			this.lootTable = containerLootComponent.lootTable();
			this.lootTableSeed = containerLootComponent.seed();
		}
	}
	
	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
		super.collectImplicitComponents(componentMapBuilder);
		if (this.lootTable != null) {
			componentMapBuilder.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.lootTable, this.lootTableSeed));
		}
	}
	
}
