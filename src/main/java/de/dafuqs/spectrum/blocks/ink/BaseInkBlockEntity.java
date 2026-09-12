package de.dafuqs.spectrum.blocks.ink;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.blocks.ink.gen.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.*;

import java.util.*;

public abstract class BaseInkBlockEntity<T extends InkStorage> extends BaseContainerBlockEntity implements PlayerOwned, InkStorageBlockEntity<T>, MenuProvider {
	
	public NonNullList<ItemStack> inventory;
	
	protected T inkStorage;
	protected boolean inkDirty;
	
	public static final int NO_INK_SLOT_ID = -1;
	public final int inkSlotId;
	
	protected boolean paused;
	protected Optional<Holder<InkColor>> selectedColor = Optional.empty();
	private @Nullable UUID ownerUUID;
	
	public BaseInkBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, T inkStorage, int inventorySize, int inkSlotId) {
		super(blockEntityType, blockPos, blockState);
		this.inkStorage = inkStorage;
		this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
		this.inkSlotId = inkSlotId;
	}
	
	@Override
	public T getInkStorage() {
		return inkStorage;
	}
	
	public InkCapability getInkCapability() {
		return getLevel().getCapability(InkCapabilities.BLOCK, getBlockPos());
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		if(!this.inventory.isEmpty()) {
			this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
		}
		this.ownerUUID = PlayerOwnedWithName.readOwnerUUID(nbt);
		if (nbt.contains("selected_color", Tag.TAG_STRING)) {
			this.selectedColor = Optional.of(SpectrumRegistries.INK_COLOR.wrapAsHolder(InkColor.ofIdString(nbt.getString("selected_color")).get()));
		} else {
			this.selectedColor = Optional.empty();
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		if(!this.inventory.isEmpty()) {
			ContainerHelper.saveAllItems(nbt, this.inventory, registryLookup);
		}
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		this.selectedColor.ifPresent(color -> nbt.putString("selected_color", color.getRegisteredName()));
	}
	
	@Override
	public @Nullable UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		setChanged();
	}
	
	@Override
	public void setChanged() {
		super.setChanged();
		this.paused = false;
		if (this.level != null && !this.level.isClientSide) {
			updateInClientWorld();
		}
	}
	
	@Override
	public void setInkDirty() {
		this.setChanged();
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
		setChanged();
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack itemStack = super.removeItem(slot, amount);
		setChanged();
		return itemStack;
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack itemStack = super.removeItemNoUpdate(slot);
		setChanged();
		return itemStack;
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		super.setItem(slot, stack);
		setChanged();
	}
	
	@Override
	public int getContainerSize() {
		return this.inventory.size();
	}
	
	protected void equalizeInkContainer(int slotId) {
		ItemStack slotStack = inventory.get(slotId);
		InkCapability inkCapability = slotStack.getCapability(InkCapabilities.ITEM, null);
		if (inkCapability != null) {
			InkCapability blockCapability = getInkCapability();
			InkTransferHelper.equalizeInk(blockCapability, inkCapability, this.selectedColor.map(Holder::value).orElse(null));
		}
	}
	
	public void setSelectedColor(Optional<Holder<InkColor>> inkColor) {
		this.selectedColor = inkColor;
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
	
	@SuppressWarnings("unused")
	public static<T extends BaseInkBlockEntity<?>> void serverTick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		if (blockEntity.paused && !blockEntity.inkDirty) {
			return;
		}
		blockEntity.inkDirty = false;
		
		if(blockEntity.inkSlotId != NO_INK_SLOT_ID) {
			blockEntity.equalizeInkContainer(blockEntity.inkSlotId);
		}
		
		if (blockEntity.shouldTickLogic(level)) {
			if(blockEntity.tickLogic(level)) {
				blockEntity.setInkDirty();
			} else {
				blockEntity.paused = true;
			}
		}
	}
	
	@Override
	public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
		BaseInkScreenHandler.ScreenOpeningData.STREAM_CODEC.encode(buffer, new BaseInkScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor));
	}
	
	public abstract boolean shouldTickLogic(Level world);
	
	public abstract boolean tickLogic(Level level);
	
}
