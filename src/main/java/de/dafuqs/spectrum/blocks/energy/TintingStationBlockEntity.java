package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class TintingStationBlockEntity extends BaseInkTransferBlockEntity<IndividualCappedInkStorage> implements MenuProvider {
	
	public static final int INPUT_SLOT_ID = 0;
	public static final int OUTPUT_SLOT_ID = 1;
	
	public static final long TICKS_PER_CONVERSION = 5;
	public static final long ITEM_COLORING_COST = 10;
	public static final long STORAGE_AMOUNT = (long) Math.pow(2, 64);
	
	public TintingStationBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.TINTING_STATION.get(), blockPos, blockState, new IndividualCappedInkStorage(STORAGE_AMOUNT));
	}
	
	@SuppressWarnings("unused")
	public static void serverTick(Level world, BlockPos pos, BlockState state, TintingStationBlockEntity blockEntity) {
		blockEntity.inkDirty = false;
		if (!blockEntity.paused) {
			boolean convertedPigment = false;
			boolean didSomething = true;
			
			Optional<Holder<InkColor>> selectedColor = blockEntity.getSelectedColor();
			InkStorage inkStorage = blockEntity.getEnergyStorage();
			
			if (selectedColor.isPresent() && world.getGameTime() % TICKS_PER_CONVERSION == 0) {
				didSomething = blockEntity.tryColorStack(selectedColor.get().value());
			}
			
			if(!didSomething) {
				didSomething = blockEntity.tryFillInkContainer();
			}
			
			if (didSomething) {
				blockEntity.setInkDirty();
				blockEntity.setChanged();
			} else {
				blockEntity.paused = true;
			}
		}
	}
	
	public boolean tryColorStack(InkColor inkColor) {
		if(inkStorage.getEnergy(inkColor) < ITEM_COLORING_COST) {
			return false;
		}
		
		ItemStack output = getItem(OUTPUT_SLOT_ID);
		if(output.getCount() >= output.getMaxStackSize()){
			return false;
		}
		
		ItemStack input = getItem(INPUT_SLOT_ID);
		Item resultItem = VariantHelper.getColoredItem(input, inkColor);
		if(resultItem == null) {
			return false;
		}
		
		ItemStack resultStack = resultItem.getDefaultInstance();
		resultStack.applyComponents(input.getComponents());
		
		if(output.isEmpty()) {
			setItem(OUTPUT_SLOT_ID, resultStack);
		} else if(ItemStack.isSameItemSameComponents(output, resultStack)) {
			input.shrink(1);
			output.grow(1);
		} else {
			return false;
		}
		
		input.shrink(1);
		inkStorage.addEnergy(inkColor, -ITEM_COLORING_COST);
		
		if (SpectrumConfig.CONFIG.BlockSoundVolume.get() > 0) {
			level.playSound(null, worldPosition, SpectrumSoundEvents.COLOR_PICKER_PROCESSING, SoundSource.BLOCKS, SpectrumConfig.CONFIG.BlockSoundVolume.get().floatValue() / 3F, 1.0F);
			
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) level,
					new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 0.7, worldPosition.getZ() + 0.5),
					ColoredFluidRisingParticleEffect.of(inkColor.getColorInt()),
					5,
					new Vec3(0.22, 0.0, 0.22),
					new Vec3(0.0, 0.1, 0.0)
			);
		}
		return true;
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
		return Component.translatable("block.spectrum.tinting_station");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new TintingStationScreenHandler(syncId, playerInventory, this, new InkTransferScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor).inkColor());
	}
	
	@Override
	public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
		InkTransferScreenHandler.ScreenOpeningData.PACKET_CODEC.encode(buffer, new InkTransferScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor));
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == OUTPUT_SLOT_ID) {
			return stack.getItem() instanceof InkStorageItem<?>;
		}
		return true;
	}
	
}
