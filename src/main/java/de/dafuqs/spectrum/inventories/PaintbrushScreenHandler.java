package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.items.magic_items.PaintBrushItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PaintbrushScreenHandler extends ScreenHandler implements InkColorSelectedPacketReceiver {
	
	private final ScreenHandlerContext context;
	private final PlayerEntity player;
	private final World world;
	private final ItemStack paintBrushStack;
	
	public PaintbrushScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ScreenHandlerContext.EMPTY, null);
	}
	
	public PaintbrushScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ItemStack paintBrushStack) {
		super(SpectrumScreenHandlerTypes.PAINTBRUSH, syncId);
		this.paintBrushStack = paintBrushStack;
		this.context = context;
		this.world = playerInventory.player.getEntityWorld();
		this.player = playerInventory.player;
	}
	
	public boolean canUse(PlayerEntity player) {
		for(ItemStack itemStack : player.getItemsHand()) {
			if(itemStack == paintBrushStack) {
				return true;
			}
		}
		return false;
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
