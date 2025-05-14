package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.tags.*;
import net.minecraft.world.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class TagFilterSlot extends Slot {
	
	private final TagKey<Item> acceptedTag;
	
	public TagFilterSlot(Container inventory, int index, int x, int y, TagKey<Item> acceptedTag) {
		super(inventory, index, x, y);
		this.acceptedTag = acceptedTag;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.is(acceptedTag);
	}
	
}
