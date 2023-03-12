package de.dafuqs.spectrum.blocks.spirit_sallow;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class OminousSaplingBlockItem extends BlockItem {
	
	
	public OminousSaplingBlockItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	public ActionResult place(ItemPlacementContext context) {
		ActionResult actionResult = super.place(context);
		
		BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
		if (blockEntity instanceof OminousSaplingBlockEntity ominousSaplingBlockEntity) {
			if (context.getPlayer() != null) {
				ominousSaplingBlockEntity.setOwner(context.getPlayer());
			}
		}
		
		return actionResult;
	}
}
