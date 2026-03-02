package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public final class BottomlessItemCapability extends BottomlessItemHandler {
	
	private final ItemStack bottomlessStack;
	
	public @NotNull static BottomlessItemCapability get(ItemStack bottomlessBundle) {
		BottomlessItemHandler handler = bottomlessBundle.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessComponent.DEFAULT).handler();
		return new BottomlessItemCapability(bottomlessBundle, handler.capacity(), handler.deletesOverflow(), handler.locked(), handler.variant(), handler.count());
	}
	
	public @NotNull static BottomlessItemCapability get(ItemStack bottomlessBundle, @Nullable HolderLookup.Provider registryLookup) {
		BottomlessItemHandler handler = BottomlessComponent.get(bottomlessBundle, registryLookup, true).handler();
		return new BottomlessItemCapability(bottomlessBundle, handler.capacity(), handler.deletesOverflow(), handler.locked(), handler.variant(), handler.count());
	}
	
	private BottomlessItemCapability(ItemStack bottomlessStack, long capacity, boolean deletesOverflow, boolean locked, ItemStack variant, long count) {
		super(capacity, deletesOverflow, locked, variant, count);
		this.bottomlessStack = bottomlessStack;
	}
	
	@Override
	public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		ItemStack result = super.insertItem(slot, stack, simulate);
		if(!simulate) {
			bottomlessStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessComponent(this));
		}
		return result;
	}
	
	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack result = super.extractItem(slot, amount, simulate);
		if(!simulate) {
			bottomlessStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessComponent(this));
		}
		return result;
	}
	
}
