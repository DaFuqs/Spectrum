package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import javax.annotation.*;

/**
 * Forge-native replacement for the Fabric SingleVariantStorage-based block entity.
 * - Removes any Fabric Transfer API usage.
 * - Provides a tiny VariantStorage inner class with the minimal surface needed by the mod.
 * - Keeps the exact NBT/layout used by BottomlessBundleItem.BottomlessStack so saved items remain compatible.
 */
public class BottomlessBundleBlockEntity extends BlockEntity {
	
	// Do not modify without syncing storage too!
	// Contents are synced from/into storage whenever needed [i.e. (de)serialization or setting/fetching bundle item]
	private ItemStack bottomlessBundleStack;
	private BottomlessItemHandler itemHandler;
	
	public BottomlessBundleBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.BOTTOMLESS_BUNDLE.get(), pos, state);
		this.bottomlessBundleStack = SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem().getDefaultInstance();
	}
	
	// Call whenever bundle/storage contents need to be synced with each other [(de)serialization, bundle stack set, bundle block break loot]
	private void syncBundleWithStorage() {
		if(this.itemHandler != null) {
			this.bottomlessBundleStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessComponent(this.itemHandler));
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		syncBundleWithStorage();
		nbt.put("Bundle", this.bottomlessBundleStack.saveOptional(registryLookup));
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.setBundle(ItemStack.parse(registryLookup, nbt.getCompound("Bundle")).orElse(SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem().getDefaultInstance()), registryLookup);
	}
	
	public void setBundle(ItemStack itemStack, HolderLookup.Provider registryLookup) {
		this.bottomlessBundleStack = itemStack;
		BottomlessComponent component = BottomlessComponent.get(this.bottomlessBundleStack,registryLookup, true);
		this.itemHandler = component.handler();
	}
	
	public ItemStack retrieveBundle() {
		if (this.bottomlessBundleStack.isEmpty()) {
			return SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem().getDefaultInstance();
		} else {
			syncBundleWithStorage();
			return this.bottomlessBundleStack;
		}
	}
	
	public BottomlessItemHandler storage() {
		return itemHandler;
	}
	
}