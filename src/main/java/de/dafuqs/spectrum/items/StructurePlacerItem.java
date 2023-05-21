package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.api.*;

import java.util.*;

public class StructurePlacerItem extends Item implements CreativeOnlyItem {
	
	protected final Identifier multiBlockIdentifier;
	
	public StructurePlacerItem(Settings settings, Identifier multiBlockIdentifier) {
		super(settings);
		this.multiBlockIdentifier = multiBlockIdentifier;
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getPlayer() != null && context.getPlayer().isCreative()) {
			IMultiblock iMultiblock = SpectrumMultiblocks.MULTIBLOCKS.get(multiBlockIdentifier);
			if (iMultiblock != null) {
				BlockRotation blockRotation = Support.rotationFromDirection(context.getPlayerFacing());
				iMultiblock.place(context.getWorld(), context.getBlockPos().up(), blockRotation);
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		CreativeOnlyItem.appendTooltip(tooltip);
	}
	
}
