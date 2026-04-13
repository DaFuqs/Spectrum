package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.recipe.color_picker.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PaintingStationBlockEntity extends BaseInkTransferBlockEntity<IndividualCappedInkStorage> implements MenuProvider {
	
	public static final long TICKS_PER_CONVERSION = 5;
	public static final long ITEM_COLORING_COST = 10;
	public static final long STORAGE_AMOUNT = (long) Math.pow(2, 64);
	protected @Nullable InkConvertingRecipe cachedRecipe;
	
	public PaintingStationBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.PAINTING_STATION.get(), blockPos, blockState, new IndividualCappedInkStorage(STORAGE_AMOUNT, Map.of()));
	}
	
	@SuppressWarnings("unused")
	public static void tick(Level world, BlockPos pos, BlockState state, PaintingStationBlockEntity blockEntity) {
		if (!world.isClientSide) {
			blockEntity.inkDirty = false;
			if (!blockEntity.paused) {
				boolean convertedPigment = false;
				boolean didSomething = true;
				
				Optional<Holder<InkColor>> selectedColor = blockEntity.getSelectedColor();
				InkStorage inkStorage = blockEntity.getEnergyStorage();
				
				if (selectedColor.isPresent() && world.getGameTime() % TICKS_PER_CONVERSION == 0) {
					if (inkStorage.getEnergy(selectedColor.get().value()) >= ITEM_COLORING_COST) {
						didSomething = blockEntity.tryColorItem(selectedColor);
					}
				} else {
					didSomething = blockEntity.tryFillInkContainer();
				}
				
				if (didSomething) {
					blockEntity.updateInClientWorld();
					blockEntity.setInkDirty();
					blockEntity.setChanged();
				} else {
					blockEntity.paused = true;
				}
			}
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		CodecHelper.fromNbt(InkStorageComponent.CODEC, nbt.get("InkStorage")).ifPresent(storage -> this.inkStorage = new IndividualCappedInkStorage(storage.maxEnergyTotal(), storage.storedEnergy()));
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		CodecHelper.writeNbt(nbt, "InkStorage", InkStorageComponent.CODEC, new InkStorageComponent(this.inkStorage));
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.painting_station");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new ColorPickerScreenHandler(syncId, playerInventory, playerInventory.player.level().getBlockEntity(new ColorPickerScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor).pos(), SpectrumBlockEntities.COLOR_PICKER.get()).orElseThrow(), new ColorPickerScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor).inkColor());
	}
	
	@Override
	public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
		ColorPickerScreenHandler.ScreenOpeningData.PACKET_CODEC.encode(buffer, new ColorPickerScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor));
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == OUTPUT_SLOT_ID) {
			return stack.getItem() instanceof InkStorageItem<?>;
		}
		return true;
	}
	
}
