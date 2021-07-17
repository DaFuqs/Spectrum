package de.dafuqs.pigment.inventories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;

public abstract class AutoInventory extends CraftingInventory {

    public AutoInventory() {
        super(null, 3, 3);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

}
