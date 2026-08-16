package de.dafuqs.spectrum.blocks.ink.gen;

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

public abstract class InkGeneratorBlockEntity extends BaseInkBlockEntity<TotalCappedInkStorage> implements MenuProvider {
	
	public static final int INK_SLOT_ID = 0;
	public static final long RUN_LOGIC_EVERY_X_TICKS = 20;
	
	public InkGeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int tier, int inventorySize) {
		super(blockEntityType, blockPos, blockState, new TotalCappedInkStorage((long) Math.pow(256, tier), Map.of()), inventorySize, INK_SLOT_ID);
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		CodecHelper.fromNbt(InkStorageComponent.CODEC, nbt.get("ink_storage")).ifPresent(storage -> this.inkStorage = new TotalCappedInkStorage(storage.maxEnergyTotal(), storage.storedEnergy()));
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		CodecHelper.writeNbt(nbt, "ink_storage", InkStorageComponent.CODEC, new InkStorageComponent(this.inkStorage));
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new BaseInkScreenHandler(syncId, playerInventory, this, this.selectedColor);
	}
	
	public boolean shouldTickLogic(Level world) {
		return world.getGameTime() % RUN_LOGIC_EVERY_X_TICKS == 0;
	}
	
	public abstract boolean tickLogic(Level level);
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == INK_SLOT_ID) {
			return stack.getCapability(InkCapabilities.ITEM) != null;
		}
		return true;
	}
	
}
