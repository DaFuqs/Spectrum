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
import de.dafuqs.spectrum.recipe.color_picker.*;
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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ColorPickerBlockEntity extends BaseInkTransferBlockEntity<TotalCappedInkStorage> implements MenuProvider {
	
	public static final long TICKS_PER_CONVERSION = 5;
	public static final long STORAGE_AMOUNT = (long) Math.pow(2, 16);
	protected @Nullable InkConvertingRecipe cachedRecipe;
	
	public ColorPickerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.COLOR_PICKER.get(), blockPos, blockState, new TotalCappedInkStorage(STORAGE_AMOUNT, Map.of()));
	}
	
	@SuppressWarnings("unused")
	public static void serverTick(Level world, BlockPos pos, BlockState state, ColorPickerBlockEntity blockEntity) {
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
				blockEntity.setChanged();
			} else if (shouldPause) {
				blockEntity.paused = true;
			}
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		CodecHelper.fromNbt(InkStorageComponent.CODEC, nbt.get("InkStorage")).ifPresent(storage -> this.inkStorage = new TotalCappedInkStorage(storage.maxEnergyTotal(), storage.storedEnergy()));
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		CodecHelper.writeNbt(nbt, "InkStorage", InkStorageComponent.CODEC, new InkStorageComponent(this.inkStorage));
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.color_picker");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new ColorPickerScreenHandler(syncId, playerInventory, this, new InkTransferScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor).inkColor());
	}
	
	@Override
	public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
		InkTransferScreenHandler.ScreenOpeningData.PACKET_CODEC.encode(buffer, new InkTransferScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor));
	}
	
	protected boolean tryConvertPigmentToEnergy(ServerLevel level) {
		InkConvertingRecipe recipe = getInkConvertingRecipe(level);
		if (recipe != null) {
			InkColor inkColor = recipe.getInkColor();
			long amount = recipe.getInkAmount();
			if (amount <= this.inkStorage.getRoom(inkColor)) {
				inventory.get(INPUT_SLOT_ID).shrink(1);
				this.inkStorage.addEnergy(inkColor, amount);
				
				if (SpectrumConfig.CONFIG.BlockSoundVolume.get() > 0) {
					level.playSound(null, worldPosition, SpectrumSoundEvents.COLOR_PICKER_PROCESSING, SoundSource.BLOCKS, SpectrumConfig.CONFIG.BlockSoundVolume.get().floatValue() / 3F, 1.0F);
				}
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(level,
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
