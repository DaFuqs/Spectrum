package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class TintingStationBlockEntity extends BaseInkTransferBlockEntity<IndividualCappedInkStorage> implements MenuProvider {
	
	public static final int INPUT_SLOT_ID = 0;
	public static final int OUTPUT_SLOT_ID = 1;
	
	public static final long TICKS_PER_CONVERSION = 5;
	public static final long ITEM_COLORING_COST = 10;
	public static final long STORAGE_AMOUNT = (long) Math.pow(2, 32);
	
	public TintingStationBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.TINTING_STATION.get(), blockPos, blockState, new IndividualCappedInkStorage(STORAGE_AMOUNT));
	}
	
	@SuppressWarnings("unused")
	public static void serverTick(Level world, BlockPos pos, BlockState state, TintingStationBlockEntity blockEntity) {
		blockEntity.inkDirty = false;
		if (blockEntity.paused) {
			return;
		}
		
		boolean didSomething = false;
		
		Optional<Holder<InkColor>> inkColorHolder = blockEntity.getSelectedColor();
		InkStorage inkStorage = blockEntity.getInkStorage();
		
		ItemStack input = blockEntity.getItem(INPUT_SLOT_ID);
		if (!input.isEmpty()) {
			if (inkColorHolder.isPresent() && world.getGameTime() % TICKS_PER_CONVERSION == 0) {
				InkColor selectedInkColor = inkColorHolder.get().value();
				if (inkStorage.getEnergy(selectedInkColor) >= ITEM_COLORING_COST) {
					ItemStack output = blockEntity.getItem(OUTPUT_SLOT_ID);
					if (output.getCount() < output.getMaxStackSize()) {
						ItemStack resultStack = blockEntity.colorStack(input, selectedInkColor);
						if (resultStack.isEmpty()) {
							resultStack = tintStack(input, selectedInkColor);
						}
						
						if (!resultStack.isEmpty()) {
							if (output.isEmpty()) {
								input.shrink(1);
								blockEntity.setItem(OUTPUT_SLOT_ID, resultStack);
								didSomething = true;
							} else if (ItemStack.isSameItemSameComponents(output, resultStack)) {
								input.shrink(1);
								output.grow(1);
								didSomething = true;
							}
							
							if (didSomething) {
								inkStorage.addEnergy(selectedInkColor, -ITEM_COLORING_COST);
								
								if (SpectrumConfig.CONFIG.BlockSoundVolume.get() > 0) {
									blockEntity.getLevel().playSound(null, blockEntity.getBlockPos(), SpectrumSoundEvents.COLOR_PICKER_PROCESSING, SoundSource.BLOCKS, SpectrumConfig.CONFIG.BlockSoundVolume.get().floatValue() / 3F, 1.0F);
									
									PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) blockEntity.getLevel(),
											new Vec3(blockEntity.getBlockPos().getX() + 0.5, blockEntity.getBlockPos().getY() + 0.7, blockEntity.getBlockPos().getZ() + 0.5),
											ColoredFluidRisingParticleEffect.of(selectedInkColor.getColorInt()),
											5,
											new Vec3(0.22, 0.0, 0.22),
											new Vec3(0.0, 0.1, 0.0)
									);
								}
							}
						}
					}
				}
			}
		}
		
		if(!didSomething) {
			didSomething = blockEntity.tryFillInkContainer();
		}
		
		if (didSomething) {
			blockEntity.setInkDirty();
		} else {
			blockEntity.paused = true;
		}
	}
	
	// Cleaned up version of net.minecraft.world.item.component.DyedItemColor
	// modified to work with InkColor
	protected static ItemStack tintStack(ItemStack stack, InkColor inkColor) {
		if (!stack.is(ItemTags.DYEABLE)) {
			return ItemStack.EMPTY;
		} else {
			ItemStack result = stack.copyWithCount(1);
			int i = 0;
			int j = 0;
			int k = 0;
			int prevMax = 0;
			int i1 = 0;
			DyedItemColor dyeditemcolor = result.get(DataComponents.DYED_COLOR);
			if (dyeditemcolor != null) {
				int prevRed = FastColor.ARGB32.red(dyeditemcolor.rgb());
				int prevGreen = FastColor.ARGB32.green(dyeditemcolor.rgb());
				int prevBlue = FastColor.ARGB32.blue(dyeditemcolor.rgb());
				prevMax += Math.max(prevRed, Math.max(prevGreen, prevBlue));
				i += prevRed;
				j += prevGreen;
				k += prevBlue;
				i1++;
			}
			
			int j3 = inkColor.getColorInt();
			int i2 = FastColor.ARGB32.red(j3);
			int j2 = FastColor.ARGB32.green(j3);
			int k2 = FastColor.ARGB32.blue(j3);
			prevMax += Math.max(i2, Math.max(j2, k2));
			i += i2;
			j += j2;
			k += k2;
			i1++;
			
			int finalRed = i / i1;
			int finalGreen = j / i1;
			int finalBlue = k / i1;
			float f = (float)prevMax / (float)i1;
			float f1 = (float)Math.max(finalRed, Math.max(finalGreen, finalBlue));
			finalRed = (int)((float)finalRed * f / f1);
			finalGreen = (int)((float)finalGreen * f / f1);
			finalBlue = (int)((float)finalBlue * f / f1);
			int finalColor = FastColor.ARGB32.color(0, finalRed, finalGreen, finalBlue);
			boolean flag = dyeditemcolor == null || dyeditemcolor.showInTooltip();
			result.set(DataComponents.DYED_COLOR, new DyedItemColor(finalColor, flag));
			return result;
		}
	}
	
	public ItemStack colorStack(ItemStack stack, InkColor inkColor) {
		Item resultItem = VariantHelper.getColoredItem(stack, inkColor);
		if(resultItem == null) {
			return ItemStack.EMPTY;
		}
		
		ItemStack resultStack = resultItem.getDefaultInstance();
		resultStack.applyComponents(stack.getComponents());
		return resultStack;
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		CodecHelper.fromNbt(InkStorageComponent.CODEC, nbt.get("ink_storage")).ifPresent(storage -> this.inkStorage = new IndividualCappedInkStorage(storage.maxPerColor(), storage.storedEnergy()));
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		CodecHelper.writeNbt(nbt, "ink_storage", InkStorageComponent.CODEC, new InkStorageComponent(this.inkStorage));
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
