package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.items.magic_items.BottomlessBundleItem;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BottomlessBundleBlockEntity extends BlockEntity {
	
	protected ItemStack voidBundleStack;
	
	public BottomlessBundleBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.BOTTOMLESS_BUNDLE, pos, state);
		this.voidBundleStack = ItemStack.EMPTY;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.voidBundleStack = ItemStack.fromNbt(nbt);
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		voidBundleStack.writeNbt(nbt);
	}
	
	public void setVoidBundle(@NotNull ItemStack itemStack) {
		if(itemStack.getItem() instanceof BottomlessBundleItem) {
			this.voidBundleStack = itemStack;
		}
	}
	
	public ItemStack getVoidBundle() {
		if(this.voidBundleStack.isEmpty()) {
			return SpectrumItems.VOID_BUNDLE.getDefaultStack();
		} else {
			return this.voidBundleStack;
		}
	}
	
}
