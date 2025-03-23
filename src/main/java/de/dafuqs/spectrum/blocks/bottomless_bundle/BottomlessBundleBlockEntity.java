package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

@SuppressWarnings("UnstableApiUsage")
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
			return variant.getItem().canBeNested()
					&& (this.variant.isBlank() || this.variant.isOf(variant.getItem()) && this.variant.nbtMatches(variant.getNbt()));
		}

		@Override
		public long insert(ItemVariant insertedVariant, long maxAmount, TransactionContext transaction) {
			long inserted = super.insert(insertedVariant, maxAmount, transaction);
			return isVoiding ? maxAmount : inserted;
		}
		
		@Override
		protected ItemVariant getBlankVariant() {
			return this.variant; // lock to the item the player set it to when placing it down
		}

		@Override
		protected long getCapacity(ItemVariant variant) {
			return BottomlessBundleItem.getMaxStoredAmount(powerLevel);
		}

		// NOTE: the bundle stack's contents *could* be synced here,
		// though that'd be very costly considering the reasonably large average amount of committed transactions.
		// Only do so if it [sync] becomes a real problem in the future,
		// e.g. when unpredictable bundle/storage changes become a thing [if ever]
		@Override
		protected void onFinalCommit() {
			super.onFinalCommit();
			markDirty();
		}
	};

	public BottomlessBundleBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.BOTTOMLESS_BUNDLE, pos, state);
		this.bottomlessBundleStack = SpectrumItems.BOTTOMLESS_BUNDLE.getDefaultStack();
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.setBundleUnsynced(ItemStack.fromNbt(nbt.getCompound("Bundle")));

		// Handle old data by syncing into bundle
		if (nbt.contains("StorageVariant")) {
			this.storage.variant = ItemVariant.fromNbt(nbt.getCompound("StorageVariant"));
			this.storage.amount = nbt.getLong("StorageCount");
			syncBundleWithStorage();
		} else syncStorageWithBundle();
	}

	// Trivial sync methods. Call whenever bundle/storage contents need to be synced with each other [(de)serialization, bundle stack set, bundle block break loot]
	private void syncBundleWithStorage() {
		if (this.storage.variant == null || this.storage.amount == 0) {
			BottomlessBundleItem.setBundledStack(this.bottomlessBundleStack, ItemStack.EMPTY, 0);
		} else {
			BottomlessBundleItem.setBundledStack(this.bottomlessBundleStack, this.storage.variant.toStack(), (int) this.storage.amount);
		}
	}

	private void syncStorageWithBundle() {
		this.storage.variant = ItemVariant.of(BottomlessBundleItem.getFirstBundledStack(bottomlessBundleStack));
		this.storage.amount = BottomlessBundleItem.getStoredAmount(bottomlessBundleStack);
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		// sync bundle data
		syncBundleWithStorage();

		NbtCompound bundleCompound = new NbtCompound();
		bottomlessBundleStack.writeNbt(bundleCompound);
		nbt.put("Bundle", bundleCompound);
	}

	private boolean setBundleUnsynced(ItemStack itemStack) {
		if (itemStack.getItem() instanceof BottomlessBundleItem) {
			this.bottomlessBundleStack = itemStack;
			// cache once, use many times
			this.isVoiding = EnchantmentHelper.getLevel(SpectrumEnchantments.VOIDING, bottomlessBundleStack) > 0;
			this.powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, itemStack);
			return true;
		}
		return false;
	}

	public void setBundle(@NotNull ItemStack itemStack) {
		if (setBundleUnsynced(itemStack)) syncStorageWithBundle();
	}

	public ItemStack retrieveBundle() {
		if (this.bottomlessBundleStack.isEmpty()) {
			return SpectrumItems.BOTTOMLESS_BUNDLE.getDefaultStack();
		} else {
			syncBundleWithStorage();
			return this.bottomlessBundleStack;
		}
	}
	
}
