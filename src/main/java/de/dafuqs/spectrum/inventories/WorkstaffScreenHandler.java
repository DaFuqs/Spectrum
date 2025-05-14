package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.items.tools.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;

public class WorkstaffScreenHandler extends QuickNavigationGridScreenHandler {
	
	private final Player player;
	private final ItemStack workstaffStack;
	
	public WorkstaffScreenHandler(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, ItemStack.EMPTY);
	}
	
	public WorkstaffScreenHandler(int syncId, Inventory playerInventory, ItemStack workstaffStack) {
		super(SpectrumScreenHandlerTypes.WORKSTAFF, syncId);
		this.workstaffStack = workstaffStack;
		this.player = playerInventory.player;
	}
	
	@Override
	public boolean stillValid(Player player) {
		for (ItemStack itemStack : player.getHandSlots()) {
			if (itemStack == workstaffStack) {
				return true;
			}
		}
		return false;
	}
	
	public void onWorkstaffToggleSelectionPacket(WorkstaffItem.GUIToggle toggle) {
		WorkstaffItem.applyToggle(player, workstaffStack, toggle);
	}
	
}
