package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.*;

public final class BottomlessCapabilityItemHandler extends BottomlessItemHandler {
	
	private final ItemStack bottomlessStack;
	
	public @NotNull static BottomlessCapabilityItemHandler get(ItemStack bottomlessBundle) {
		@Nullable BottomlessItemHandler handler = bottomlessBundle.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessItemHandlerComponent.DEFAULT).handler();
		return new BottomlessCapabilityItemHandler(bottomlessBundle, handler.capacity(), handler.deletesOverflow(), handler.locked(), handler.variant(), handler.count());
	}
	
	private BottomlessCapabilityItemHandler(ItemStack bottomlessStack, long capacity, boolean deletesOverflow, boolean locked, ItemStack variant, long count) {
		super(capacity, deletesOverflow, locked, variant, count);
		this.bottomlessStack = bottomlessStack;
	}
	
	@Override
	public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		ItemStack result = super.insertItem(slot, stack, simulate);
		if(!simulate) {
			bottomlessStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessItemHandlerComponent(this));
		}
		return result;
	}
	
	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack result = super.extractItem(slot, amount, simulate);
		if(!simulate) {
			bottomlessStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessItemHandlerComponent(this));
		}
		return result;
	}
	
}
