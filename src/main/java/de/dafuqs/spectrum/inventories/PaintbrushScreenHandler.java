package de.dafuqs.spectrum.inventories;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.items.magic_items.PaintBrushItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import org.jetbrains.annotations.Nullable;

public class PaintbrushScreenHandler extends ScreenHandler implements InkColorSelectedPacketReceiver {
	
	private final PlayerEntity player;
	private final ItemStack paintBrushStack;
	private boolean hasAccessToWhites;
	
	public PaintbrushScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ScreenHandlerContext.EMPTY, null);
	}
	
	public PaintbrushScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ItemStack paintBrushStack) {
		super(SpectrumScreenHandlerTypes.PAINTBRUSH, syncId);
		this.player = playerInventory.player;
		this.paintBrushStack = paintBrushStack;
		this.hasAccessToWhites = AdvancementHelper.hasAdvancement(playerInventory.player, InkColors.WHITE.getRequiredAdvancement());
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}
	
	public boolean canUse(PlayerEntity player) {
		for(ItemStack itemStack : player.getHandItems()) {
			if(itemStack == paintBrushStack) {
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
		PaintBrushItem.setColor(paintBrushStack, inkColor);
	}
	
	@Override
	public BlockEntity getBlockEntity() {
		return null;
	}
	
}
