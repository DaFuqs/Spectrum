package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;

public class BagOfHoldingScreenHandler extends GenericContainerScreenHandler {
	
	public BagOfHoldingScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(9 * 3));
	}
	
	public BagOfHoldingScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(SpectrumScreenHandlerTypes.BAG_OF_HOLDING, syncId, playerInventory, inventory, 3);
	}
	
	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (slotIndex > 0 && isValid(slotIndex) && this.slots.get(slotIndex).getStack().isOf(SpectrumItems.BAG_OF_HOLDING)) {
			return;
		}
		super.onSlotClick(slotIndex, button, actionType, player);
	}
	
}
