package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class BaseInkTransferBlockEntity<T extends InkStorage> extends RandomizableContainerBlockEntity implements PlayerOwned, InkStorageBlockEntity<T>, MenuProvider {
	
	public static final int INVENTORY_SIZE = 2; // input & output slots
	public static final int INPUT_SLOT_ID = 0;
	public static final int OUTPUT_SLOT_ID = 1;
	
	public NonNullList<ItemStack> inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
	protected T inkStorage;
	protected boolean paused;
	protected boolean inkDirty;
	protected Optional<Holder<InkColor>> selectedColor = Optional.empty();
	private UUID ownerUUID;
	
	public BaseInkTransferBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, T inkStorage) {
		super(blockEntityType, blockPos, blockState);
		this.inkStorage = inkStorage;
	}
	
	@Override
	public T getEnergyStorage() {
		return inkStorage;
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(nbt)) {
			ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
		}
		this.ownerUUID = PlayerOwnedWithName.readOwnerUUID(nbt);
		if (nbt.contains("SelectedColor", Tag.TAG_STRING)) {
			this.selectedColor = Optional.of(SpectrumRegistries.INK_COLOR.wrapAsHolder(InkColor.ofIdString(nbt.getString("SelectedColor")).get()));
		} else {
			this.selectedColor = Optional.empty();
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		if (!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, this.inventory, registryLookup);
		}
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		this.selectedColor.ifPresent(color -> nbt.putString("SelectedColor", color.getRegisteredName()));
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		setChanged();
	}
	
	@Override
	public void setInkDirty() {
		this.inkDirty = true;
	}
	
	@Override
	public boolean getInkDirty() {
		return inkDirty;
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.inventory;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> list) {
		this.inventory = list;
		this.paused = false;
		updateInClientWorld();
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack itemStack = super.removeItem(slot, amount);
		this.paused = false;
		updateInClientWorld();
		return itemStack;
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack itemStack = super.removeItemNoUpdate(slot);
		this.paused = false;
		updateInClientWorld();
		return itemStack;
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		super.setItem(slot, stack);
		this.paused = false;
		updateInClientWorld();
	}
	
	@Override
	public int getContainerSize() {
		return INVENTORY_SIZE;
	}
	
	public boolean tryFillInkContainer() {
		long transferredAmount = 0;
		
		ItemStack stack = inventory.get(OUTPUT_SLOT_ID);
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			InkStorage itemStorage = inkStorageItem.getEnergyStorage(stack);
			
			ServerPlayer owner = null;
			if (getOwnerIfOnline(this.getLevel()) instanceof ServerPlayer serverPlayerEntity) {
				owner = serverPlayerEntity;
			}
			
			if (this.selectedColor.isEmpty()) {
				for (InkColor color : InkColors.all()) {
					transferredAmount += tryTransferInk(owner, stack, itemStorage, color);
				}
			} else {
				transferredAmount = tryTransferInk(owner, stack, itemStorage, this.selectedColor.get().value());
			}
			
			if (transferredAmount > 0) {
				inkStorageItem.setEnergyStorage(stack, itemStorage);
			}
		}
		
		return transferredAmount > 0;
	}
	
	private long tryTransferInk(ServerPlayer owner, ItemStack stack, InkStorage itemStorage, InkColor color) {
		long amount = InkStorage.transferInk(getEnergyStorage(), itemStorage, color);
		if (amount > 0 && owner != null) {
			SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(owner, stack, itemStorage, color, amount);
		}
		return amount;
	}
	
	public void setSelectedColor(Optional<Holder<InkColor>> inkColor) {
		this.selectedColor = inkColor;
		this.paused = false;
		this.setChanged();
	}
	
	public Optional<Holder<InkColor>> getSelectedColor() {
		return this.selectedColor;
	}
	
	// Called when the chunk is first loaded to initialize this be
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		CompoundTag nbtCompound = new CompoundTag();
		this.saveAdditional(nbtCompound, registryLookup);
		return nbtCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	public void updateInClientWorld() {
		if (level != null) {
			level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), Block.UPDATE_INVISIBLE);
		}
	}
	
}
