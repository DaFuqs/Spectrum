package de.dafuqs.spectrum.blocks.spirit_sallow;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;

public class OminousSaplingBlockItem extends BlockItem {
	
	
	public OminousSaplingBlockItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	public ActionResult place(ItemPlacementContext context) {
		ActionResult actionResult = super.place(context);
		
		BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
		if (blockEntity instanceof OminousSaplingBlockEntity) {
			OminousSaplingBlockEntity ominousSaplingBlockEntity = (OminousSaplingBlockEntity) blockEntity;
			if (context.getPlayer() != null) {
				ominousSaplingBlockEntity.setOwner(context.getPlayer());
			}
		}
		
		return actionResult;
	}
}
