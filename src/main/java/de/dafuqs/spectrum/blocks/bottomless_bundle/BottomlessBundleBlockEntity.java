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

public class BottomlessBundleBlockEntity extends BlockEntity {
	
	// Do not modify without syncing storage too!
	// Contents are synced from/into storage whenever needed [i.e. (de)serialization or setting/fetching bundle item]
	private ItemStack bottomlessBundleStack;
	
	// Cached to prevent incessant enchantment calls.
	// No need to write that back into the bundle stack.
	private boolean isVoiding;
	protected int powerLevel;
	
	public final SingleVariantStorage<ItemVariant> storage = new SingleVariantStorage<>() {
		
		@Override
		protected boolean canInsert(ItemVariant variant) {
			return variant.getItem().canFitInsideContainerItems()
					&& (this.variant.isBlank() || this.variant.isOf(variant.getItem())
					&& ItemStack.isSameItemSameComponents(this.variant.toStack(), variant.toStack()));
		}
		
		@Override
		public long insert(ItemVariant insertedVariant, long maxAmount, TransactionContext transaction) {
			long inserted = super.insert(insertedVariant, maxAmount, transaction);
			return isVoiding ? maxAmount : inserted;
		}
		
		@Override
		protected ItemVariant getBlankVariant() {
			// lock to the item the player set it to when placing it down
			// variant will only ever be null upon initialization, where it'll be set to the bundle
			return this.variant == null ? ItemVariant.blank() : this.variant;
		}
		
		@Override
		protected long getCapacity(ItemStack variant) {
			return BottomlessBundleItem.getMaxStoredAmount(powerLevel);
		}
		
		// NOTE: the bundle stack's contents *could* be synced here,
		// though that'd be very costly considering the reasonably large average amount of committed transactions.
		// Only do so if it [sync] becomes a real problem in the future,
		// e.g. when unpredictable bundle/storage changes become a thing [if ever]
		@Override
		protected void onFinalCommit() {
			super.onFinalCommit();
			setChanged();
		}
	};
	
	public BottomlessBundleBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.BOTTOMLESS_BUNDLE.get(), pos, state);
		this.bottomlessBundleStack = SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem().getDefaultInstance();
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
		builder.set(this.storage);
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
			// cache once, use many times
			this.isVoiding = EnchantmentHelper.hasTag(bottomlessBundleStack, SpectrumEnchantmentTags.DELETES_OVERFLOW);
			this.powerLevel = EnchantmentHelper.getItemEnchantmentLevel(registryLookup.lookup(Registries.ENCHANTMENT).flatMap(impl -> impl.get(Enchantments.POWER)).orElse(null), itemStack);
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
