package de.dafuqs.spectrum.inventories;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import org.jetbrains.annotations.*;

public abstract class QuickNavigationGridScreenHandler extends ScreenHandler {
	
	public QuickNavigationGridScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}
	
	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}
	
}
