package de.dafuqs.spectrum.blocks.upgrade;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class UpgradeBlockItem extends BlockItem {
	
	private final String tooltipString;
	
	public UpgradeBlockItem(Block block, Settings settings, String tooltipString) {
		super(block, settings);
		this.tooltipString = tooltipString;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(Text.translatable("item.spectrum." + this.tooltipString + ".tooltip").formatted(Formatting.GRAY));
	}
	
}
