package de.dafuqs.spectrum.inventories;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public class PaintbrushScreenHandler extends QuickNavigationGridScreenHandler implements InkColorSelectedPacketReceiver {
	
	private final Player player;
	private final ItemStack paintBrushStack;
	private final boolean hasAccessToWhites;
	
	public PaintbrushScreenHandler(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, ItemStack.EMPTY);
	}
	
	public PaintbrushScreenHandler(int syncId, Inventory playerInventory, ItemStack paintBrushStack) {
		super(SpectrumScreenHandlerTypes.PAINTBRUSH, syncId);
		this.player = playerInventory.player;
		this.paintBrushStack = paintBrushStack;
		this.hasAccessToWhites = AdvancementHelper.hasAdvancement(playerInventory.player, InkColors.WHITE.getRequiredAdvancement());
	}
	
	@Override
	public boolean stillValid(Player player) {
		for (ItemStack itemStack : player.getHandSlots()) {
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
	public void onInkColorSelectedPacket(Optional<Holder<InkColor>> inkColor) {
		PaintbrushItem.setColor(paintBrushStack, inkColor.map(Holder::value).orElse(null));
		removed(player);
	}
	
	@Override
	public BlockEntity getBlockEntity() {
		return null;
	}
	
}
