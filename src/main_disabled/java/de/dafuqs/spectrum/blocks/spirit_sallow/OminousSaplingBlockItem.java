package de.dafuqs.spectrum.blocks.spirit_sallow;

import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;

public class OminousSaplingBlockItem extends BlockItem {
	
	
	public OminousSaplingBlockItem(Block block, Properties settings) {
		super(block, settings);
	}
	
	@Override
	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult actionResult = super.place(context);
		
		BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
		if (blockEntity instanceof OminousSaplingBlockEntity ominousSaplingBlockEntity) {
			if (context.getPlayer() != null) {
				ominousSaplingBlockEntity.setOwner(context.getPlayer());
			}
		}
		
		return actionResult;
	}
}
