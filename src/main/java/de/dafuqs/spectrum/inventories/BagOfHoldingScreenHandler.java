package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public class BagOfHoldingScreenHandler extends ChestMenu {
	
	public BagOfHoldingScreenHandler(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(9 * 3));
	}
	
	public BagOfHoldingScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
		super(SpectrumMenuTypes.BAG_OF_HOLDING, syncId, playerInventory, inventory, 3);
	}
	
	@Override
	public void clicked(int slotIndex, int button, @NotNull ClickType actionType, @NotNull Player player) {
		if (slotIndex > 0 && isValidSlotIndex(slotIndex) && this.slots.get(slotIndex).getItem().is(SpectrumItems.BAG_OF_HOLDING)) {
			return;
		}
		super.clicked(slotIndex, button, actionType, player);
	}
	
	@Override
	public @NotNull ItemStack quickMoveStack(Player player, int slotIndex) {
		if (slotIndex > 0 && isValidSlotIndex(slotIndex) && this.slots.get(slotIndex).getItem().is(SpectrumItems.BAG_OF_HOLDING)) {
			return ItemStack.EMPTY;
		}
		return super.quickMoveStack(player, slotIndex);
	}
	
}
