package de.dafuqs.spectrum.inventories;

import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public abstract class QuickNavigationGridScreenHandler extends AbstractContainerMenu {
	
	public QuickNavigationGridScreenHandler(@Nullable MenuType<?> type, int syncId) {
		super(type, syncId);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}
	
}
