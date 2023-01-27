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

public class BottomlessBundleBlockEntity extends BlockEntity {

	protected ItemStack bottomlessBundleStack;

	public final SingleVariantStorage<ItemVariant> storage = new SingleVariantStorage<>() {
		@Override
		protected boolean canInsert(ItemVariant variant) {
			ItemStack bundledStack = BottomlessBundleItem.getFirstBundledStack(bottomlessBundleStack);
			return bundledStack.isEmpty() || variant.matches(bundledStack);
		}

		@Override
		public long insert(ItemVariant insertedVariant, long maxAmount, TransactionContext transaction) {
			long inserted = super.insert(insertedVariant, maxAmount, transaction);
			if (EnchantmentHelper.getLevel(SpectrumEnchantments.VOIDING, bottomlessBundleStack) > 0) {
				return maxAmount;
			}
			return inserted;
		}

		@Override
		protected ItemVariant getBlankVariant() {
			return ItemVariant.blank();
		}

		@Override
		protected long getCapacity(ItemVariant variant) {
			return BottomlessBundleItem.getMaxStoredAmount(bottomlessBundleStack);
		}

		@Override
		protected void onFinalCommit() {
			super.onFinalCommit();
			markDirty();
		}
	};

	public BottomlessBundleBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.BOTTOMLESS_BUNDLE, pos, state);
		this.bottomlessBundleStack = ItemStack.EMPTY;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.bottomlessBundleStack = ItemStack.fromNbt(nbt.getCompound("Bundle"));

		this.storage.variant = ItemVariant.fromNbt(nbt.getCompound("StorageVariant"));
		this.storage.amount = nbt.getLong("StorageCount");
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		NbtCompound bundleCompound = new NbtCompound();
		bottomlessBundleStack.writeNbt(bundleCompound);
		nbt.put("Bundle", bundleCompound);

		nbt.put("StorageVariant", this.storage.variant.toNbt());
		nbt.putLong("StorageCount", this.storage.amount);
	}

	public void setBundle(@NotNull ItemStack itemStack) {
		if (itemStack.getItem() instanceof BottomlessBundleItem) {
			this.bottomlessBundleStack = itemStack;
			this.storage.variant = ItemVariant.of(BottomlessBundleItem.getFirstBundledStack(itemStack));
			this.storage.amount = BottomlessBundleItem.getStoredAmount(itemStack);
		}
	}

	public ItemStack retrieveBundle() {
		if (this.bottomlessBundleStack.isEmpty()) {
			return SpectrumItems.BOTTOMLESS_BUNDLE.getDefaultStack();
		} else {
			BottomlessBundleItem.setBundledStack(this.bottomlessBundleStack, this.storage.getResource().toStack(), (int) this.storage.amount);
			return this.bottomlessBundleStack;
		}
	}
	
}
