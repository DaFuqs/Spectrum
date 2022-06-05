package de.dafuqs.spectrum.inventories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;

public abstract class AutoInventory extends CraftingInventory {
	
	public AutoInventory(int width, int height) {
		super(null, width, height);
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}
	
}
