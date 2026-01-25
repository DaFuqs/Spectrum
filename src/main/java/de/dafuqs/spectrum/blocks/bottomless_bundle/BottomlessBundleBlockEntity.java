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

import java.util.*;

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
	
	// Cached to prevent incessant enchantment calls.
	// No need to write that back into the bundle stack.
	private boolean isVoiding;
	protected int powerLevel;
	
	/**
	 * Minimal Forge-native storage equivalent to Fabric's SingleVariantStorage<ItemVariant> that this mod used.
	 * Exposes public fields `variant` and `amount` so existing call-sites (the builder etc.) can access them directly.
	 */
	public final class VariantStorage {
		/**
		 * The template variant as a single-item ItemStack (count always expected to be 1 when non-empty).
		 */
		public ItemStack variant = ItemStack.EMPTY;
		/**
		 * The stored amount (may exceed Integer.MAX_VALUE).
		 */
		public long amount = 0L;
		
		protected boolean canInsert(ItemStack toInsert) {
			// must be an item that can be stored & same item type/components as existing template (if set)
			if (toInsert.isEmpty()) return false;
			if (!toInsert.getItem().canFitInsideContainerItems()) return false;
			if (this.variant.isEmpty()) return true;
			return ItemStack.isSameItemSameComponents(this.variant, toInsert);
		}
		
		/**
		 * Immediate (non-transactional) insert. Returns the amount actually inserted.
		 * Mirrors the behavior used previously: if the bundle is voiding, callers expect the returned
		 * value to effectively indicate success for the attempted amount, so we return maxAmount in that case.
		 */
		public long insert(ItemStack insertedVariant, long maxAmount) {
			if (!canInsert(insertedVariant)) return 0L;
			long capacity = getCapacity(insertedVariant);
			long space = capacity - this.amount;
			if (space <= 0L) return 0L;
			long toInsert = Math.min(space, maxAmount);
			if (this.variant.isEmpty()) {
				// Lock template to one copy of the item
				this.variant = insertedVariant.copyWithCount(1);
			}
			this.amount += toInsert;
			return isVoiding ? maxAmount : toInsert;
		}
		
		protected ItemStack getBlankVariant() {
			return this.variant == null ? ItemStack.EMPTY : this.variant;
		}
		
		protected long getCapacity(ItemStack variant) {
			return BottomlessBundleItem.getMaxStoredAmount(powerLevel);
		}
		
		/**
		 * Called after a final commit in Fabric impl; here we forward to mark the block entity changed.
		 */
		protected void onFinalCommit() {
			setChanged();
		}
	}
	
	public final VariantStorage storage = new VariantStorage();
	
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