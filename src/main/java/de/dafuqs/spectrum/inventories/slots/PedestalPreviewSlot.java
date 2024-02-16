package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.api.gui.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class PedestalPreviewSlot extends ReadOnlySlot implements SlotWithOnClickAction {
	public PedestalPreviewSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public ItemStack getStack() {
		if (this.inventory instanceof PedestalBlockEntity pedestalBlockEntity) {
			return pedestalBlockEntity.getCurrentCraftingRecipeOutput();
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean onClicked(ItemStack heldStack, ClickType type, PlayerEntity player) {
		if (this.inventory instanceof PedestalBlockEntity pedestalBlockEntity) {
			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				if (pedestalBlockEntity.currentRecipe != null) {
					Support.grantAdvancementCriterion(serverPlayerEntity, "fail_to_take_item_out_of_pedestal", "try_take_out_item_from_pedestal");
				}
			}
		}
		return false;
	}
}
