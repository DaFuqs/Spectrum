package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IMultiblock;

public class StructurePlacerItem extends Item {
	
	Identifier multiBlockIdentifier;
	
	public StructurePlacerItem(Settings settings, Identifier multiBlockIdentifier) {
		super(settings);
		this.multiBlockIdentifier = multiBlockIdentifier;
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getPlayer() != null && context.getPlayer().isCreative()) {
			IMultiblock iMultiblock = SpectrumMultiblocks.MULTIBLOCKS.get(multiBlockIdentifier);
			if (iMultiblock != null) {
				BlockRotation blockRotation;
				switch (context.getPlayerFacing()) {
					case EAST -> {
						blockRotation = BlockRotation.CLOCKWISE_180;
					}
					case SOUTH -> {
						blockRotation = BlockRotation.COUNTERCLOCKWISE_90;
					}
					case WEST -> {
						blockRotation = BlockRotation.NONE;
					}
					default -> {
						blockRotation = BlockRotation.CLOCKWISE_90;
					}
				}
				iMultiblock.place(context.getWorld(), context.getBlockPos().up(), blockRotation);
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}
	
	
}
