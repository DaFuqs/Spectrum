package de.dafuqs.spectrum.blocks.upgrade;

import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class UpgradeBlockItem extends BlockItem {
	
	private final String tooltipString;
	
	public UpgradeBlockItem(Block block, Properties settings, String tooltipString) {
		super(block, settings);
		this.tooltipString = tooltipString;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum." + this.tooltipString + ".tooltip").withStyle(ChatFormatting.GRAY));
	}
	
}
