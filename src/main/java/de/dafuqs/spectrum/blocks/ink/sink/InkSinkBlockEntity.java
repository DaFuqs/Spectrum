package de.dafuqs.spectrum.blocks.ink.sink;

import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public abstract class InkSinkBlockEntity extends BaseInkBlockEntity<IndividualCappedInkStorage> implements MenuProvider {
	
	public InkSinkBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int tier, int inventorySize, int inkSlotId) {
		super(blockEntityType, blockPos, blockState, new IndividualCappedInkStorage((long) Math.pow(256, tier)), inventorySize, inkSlotId);
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
	public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
		BaseInkScreenHandler.ScreenOpeningData.STREAM_CODEC.encode(buffer, new BaseInkScreenHandler.ScreenOpeningData(this.worldPosition, this.selectedColor));
	}
	
}
