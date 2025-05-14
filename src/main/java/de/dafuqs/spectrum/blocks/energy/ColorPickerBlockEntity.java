package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ColorPickerBlockEntity extends RandomizableContainerBlockEntity implements PlayerOwned, InkStorageBlockEntity<TotalCappedInkStorage>, ExtendedScreenHandlerFactory<ColorPickerScreenHandler.ScreenOpeningData> {
	
	public static final int INVENTORY_SIZE = 2; // input & output slots
	public static final int INPUT_SLOT_ID = 0;
	public static final int OUTPUT_SLOT_ID = 1;
	public static final long TICKS_PER_CONVERSION = 5;
	public static final long STORAGE_AMOUNT = 64 * 64 * 64 * 100;
	
	public NonNullList<ItemStack> inventory;
	protected TotalCappedInkStorage inkStorage;
	protected boolean paused;
	protected boolean inkDirty;
	protected @Nullable InkConvertingRecipe cachedRecipe;
	protected Optional<Holder<InkColor>> selectedColor = Optional.empty();
	private UUID ownerUUID;
	
	public ColorPickerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.COLOR_PICKER, blockPos, blockState);
		
		this.inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
		this.inkStorage = new TotalCappedInkStorage(STORAGE_AMOUNT, Map.of());
	}
	
	@SuppressWarnings("unused")
	public static void tick(Level world, BlockPos pos, BlockState state, ColorPickerBlockEntity blockEntity) {
		if (!world.isClientSide) {
			blockEntity.inkDirty = false;
			if (!blockEntity.paused) {
				boolean convertedPigment = false;
				boolean shouldPause = true;
				if (world.getGameTime() % TICKS_PER_CONVERSION == 0) {
					convertedPigment = blockEntity.tryConvertPigmentToEnergy((ServerLevel) world);
				} else {
					shouldPause = false;
				}
				boolean filledContainer = blockEntity.tryFillInkContainer(); // that's an OR
				
				if (convertedPigment || filledContainer) {
					blockEntity.updateInClientWorld();
					blockEntity.setInkDirty();
					blockEntity.setChanged();
				} else if (shouldPause) {
					blockEntity.paused = true;
				}
			}
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(nbt)) {
			ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
		}
		CodecHelper.fromNbt(InkStorageComponent.CODEC, nbt.get("InkStorage")).ifPresent(storage -> this.inkStorage = new TotalCappedInkStorage(storage.maxEnergyTotal(), storage.storedEnergy()));
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
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
		CodecHelper.writeNbt(nbt, "InkStorage", InkStorageComponent.CODEC, new InkStorageComponent(this.inkStorage));
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		this.selectedColor.ifPresent(color -> nbt.putString("SelectedColor", color.getRegisteredName()));
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.color_picker");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new ColorPickerScreenHandler(syncId, playerInventory, new ColorPickerScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor));
	}
	
	@Override
	public ColorPickerScreenHandler.ScreenOpeningData getScreenOpeningData(ServerPlayer serverPlayerEntity) {
		return new ColorPickerScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor);
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
	public TotalCappedInkStorage getEnergyStorage() {
		return inkStorage;
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
	
	protected boolean tryConvertPigmentToEnergy(ServerLevel world) {
		InkConvertingRecipe recipe = getInkConvertingRecipe(world);
		if (recipe != null) {
			InkColor inkColor = recipe.getInkColor();
			long amount = recipe.getInkAmount();
			if (amount <= this.inkStorage.getRoom(inkColor)) {
				inventory.get(INPUT_SLOT_ID).shrink(1);
				this.inkStorage.addEnergy(inkColor, amount);
				
				if (SpectrumCommon.CONFIG.BlockSoundVolume > 0) {
					world.playSound(null, worldPosition, SpectrumSoundEvents.COLOR_PICKER_PROCESSING, SoundSource.BLOCKS, SpectrumCommon.CONFIG.BlockSoundVolume / 3, 1.0F);
				}
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world,
						new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 0.7, worldPosition.getZ() + 0.5),
						ColoredFluidRisingParticleEffect.of(inkColor.getColorInt()),
						5,
						new Vec3(0.22, 0.0, 0.22),
						new Vec3(0.0, 0.1, 0.0)
				);
				
				return true;
			}
		}
		return false;
	}
	
	protected @Nullable InkConvertingRecipe getInkConvertingRecipe(Level world) {
		// is the current stack empty?
		ItemStack inputStack = inventory.get(INPUT_SLOT_ID);
		if (inputStack.isEmpty()) {
			this.cachedRecipe = null;
			return null;
		}
		
		// does the cached recipe match?
		if (this.cachedRecipe != null) {
			if (this.cachedRecipe.getIngredients().get(INPUT_SLOT_ID).test(inputStack)) {
				return this.cachedRecipe;
			}
		}
		
		// search matching recipe
		Optional<RecipeHolder<InkConvertingRecipe>> recipe = world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.INK_CONVERTING, new SingleRecipeInput(inventory.get(INPUT_SLOT_ID)), world);
		if (recipe.isPresent()) {
			this.cachedRecipe = recipe.get().value();
			return this.cachedRecipe;
		} else {
			this.cachedRecipe = null;
			return null;
		}
	}
	
	protected boolean tryFillInkContainer() {
		long transferredAmount = 0;
		
		ItemStack stack = inventory.get(OUTPUT_SLOT_ID);
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			InkStorage itemStorage = inkStorageItem.getEnergyStorage(stack);
			
			ServerPlayer owner = null;
			if (getOwnerIfOnline() instanceof ServerPlayer serverPlayerEntity) {
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
		long amount = InkStorage.transferInk(this.inkStorage, itemStorage, color);
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
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == INPUT_SLOT_ID) {
			return InkConvertingRecipe.isInput(stack.getItem());
		}
		if (slot == OUTPUT_SLOT_ID) {
			return stack.getItem() instanceof InkStorageItem<?>;
		}
		return true;
	}
	
}
