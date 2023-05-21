package de.dafuqs.spectrum.inventories;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

public class PaintbrushScreenHandler extends QuickNavigationGridScreenHandler implements InkColorSelectedPacketReceiver {
	
	private final PlayerEntity player;
	private final ItemStack paintBrushStack;
	private final boolean hasAccessToWhites;
	
	public PaintbrushScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ItemStack.EMPTY);
	}
	
	public PaintbrushScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack paintBrushStack) {
		super(SpectrumScreenHandlerTypes.PAINTBRUSH, syncId);
		this.player = playerInventory.player;
		this.paintBrushStack = paintBrushStack;
		this.hasAccessToWhites = AdvancementHelper.hasAdvancement(playerInventory.player, InkColors.WHITE.getRequiredAdvancement());
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		for (ItemStack itemStack : player.getHandItems()) {
			if (itemStack == paintBrushStack) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasAccessToWhites() {
		return hasAccessToWhites;
	}
	
	@Override
	public void onInkColorSelectedPacket(@Nullable InkColor inkColor) {
		PaintbrushItem.setColor(paintBrushStack, inkColor);
		close(player);
	}
	
	@Override
	public BlockEntity getBlockEntity() {
		return null;
	}
	
}
