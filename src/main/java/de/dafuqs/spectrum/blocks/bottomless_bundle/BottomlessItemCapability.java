package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import org.jspecify.annotations.Nullable;

public final class BottomlessItemCapability extends BottomlessItemHandler {
	
	private final ItemStack bottomlessStack;
	
	public static BottomlessItemCapability get(ItemStack bottomlessBundle) {
		BottomlessItemHandler handler = bottomlessBundle.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessComponent.DEFAULT).handler();
		return new BottomlessItemCapability(bottomlessBundle, handler.capacity(), handler.deletesOverflow(), handler.locked(), handler.variant(), handler.count());
	}
	
	public static BottomlessItemCapability get(ItemStack bottomlessBundle, HolderLookup.@Nullable Provider registryLookup) {
		BottomlessItemHandler handler = BottomlessComponent.get(bottomlessBundle, registryLookup, true).handler();
		return new BottomlessItemCapability(bottomlessBundle, handler.capacity(), handler.deletesOverflow(), handler.locked(), handler.variant(), handler.count());
	}
	
	private BottomlessItemCapability(ItemStack bottomlessStack, long capacity, boolean deletesOverflow, boolean locked, ItemStack variant, long count) {
		super(capacity, deletesOverflow, locked, variant, count);
		this.bottomlessStack = bottomlessStack;
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		ItemStack result = super.insertItem(slot, stack, simulate);
		if(!simulate) {
			bottomlessStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessComponent(this));
		}
		return result;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack result = super.extractItem(slot, amount, simulate);
		if(!simulate) {
			bottomlessStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessComponent(this));
		}
		return result;
	}
	
}
