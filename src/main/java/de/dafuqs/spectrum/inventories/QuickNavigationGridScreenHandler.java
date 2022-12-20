package de.dafuqs.spectrum.inventories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public abstract class QuickNavigationGridScreenHandler extends ScreenHandler {
	
	public QuickNavigationGridScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}
	
}
