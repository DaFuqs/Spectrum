package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

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
	public BottomlessItemHandler storage;
	
	public BottomlessBundleBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.BOTTOMLESS_BUNDLE.get(), pos, state);
		this.bottomlessBundleStack = SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem().getDefaultInstance();
		this.storage = new BottomlessItemHandler(0, false);
		
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.setBundleUnsynced(ItemStack.parse(registryLookup, nbt.getCompound("Bundle"))
				.orElse(SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem().getDefaultInstance()), registryLookup);
		syncStorageWithBundle();
	}
	
	// Trivial sync methods. Call whenever bundle/storage contents need to be synced with each other [(de)serialization, bundle stack set, bundle block break loot]
	private void syncBundleWithStorage() {
		var builder = BottomlessBundleItem.BottomlessStack.Builder.of(this.level, this.bottomlessBundleStack);
		// Use the Forge-native storage fields (variant ItemStack and amount long)
		builder.set(this.storage.variant, this.storage.amount);
		builder.buildAndSet(this.bottomlessBundleStack);
	}
	
	private void syncStorageWithBundle() {
		this.storage.variant = BottomlessBundleItem.getTemplateVariant(bottomlessBundleStack);
		this.storage.amount = BottomlessBundleItem.getStoredAmount(bottomlessBundleStack);
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		syncBundleWithStorage();
		nbt.put("Bundle", this.bottomlessBundleStack.saveOptional(registryLookup));
	}
	
	private boolean setBundleUnsynced(ItemStack itemStack, HolderLookup.Provider registryLookup) {
		if (itemStack.getItem() instanceof BottomlessBundleItem) {
			this.bottomlessBundleStack = itemStack;
			
			boolean voiding = EnchantmentHelper.hasTag(bottomlessBundleStack, SpectrumEnchantmentTags.DELETES_OVERFLOW);
			int power = itemStack.getEnchantmentLevel(registryLookup.lookup(Registries.ENCHANTMENT).flatMap(impl -> impl.get(Enchantments.POWER)).orElse(null));
			this.storage = new BottomlessItemHandler(BottomlessBundleItem.getMaxStoredAmount(power), voiding);
			return true;
		}
		return false;
	}
	
	public void setBundle(@NotNull ItemStack itemStack, HolderLookup.Provider registryLookup) {
		if (setBundleUnsynced(itemStack, registryLookup)) syncStorageWithBundle();
	}
	
	public ItemStack retrieveBundle() {
		if (this.bottomlessBundleStack.isEmpty()) {
			return SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem().getDefaultInstance();
		} else {
			syncBundleWithStorage();
			return this.bottomlessBundleStack;
		}
	}
	
}