package de.dafuqs.spectrum.api.ink.capability;

import de.dafuqs.spectrum.api.ink.storage.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import javax.annotation.*;

public class ItemInkCapability implements InkCapability {
	protected final ItemStack stack;
	protected final InkStorageItem<?> item;
	protected final InkStorage storage;
	
	public static @Nullable ItemInkCapability of(ItemStack stack) {
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			return new ItemInkCapability(stack, inkStorageItem);
		}
		return null;
	}
	
	private ItemInkCapability(ItemStack stack, InkStorageItem<?> inkStorageItem) {
		this.stack = stack;
		this.item = inkStorageItem;
		this.storage = inkStorageItem.getEnergyStorage(stack);
	}
	
	public InkStorage getStorage() {
		return storage;
	}
	
	public void markDirty() {
		item.setEnergyStorage(stack, storage);
	}
	
}
