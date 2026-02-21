package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.api.block.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class FilterSlot extends ShadowSlot {
	
	FilterConfigurable filterConfigurable;
	
	public FilterSlot(FilterConfigurable filterConfigurable, Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.filterConfigurable = filterConfigurable;
	}
	
	@Override
	public boolean onClicked(ItemStack heldStack, ClickAction type, Player player) {
		filterConfigurable.setFilterItem(getContainerSlot(), ItemVariant.of(heldStack));
		return super.onClicked(heldStack, type, player);
	}
}
