package de.dafuqs.spectrum.blocks.upgrade;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class UpgradeBlockItem extends BlockItem {
	
	private final String tooltipString;
	
	public UpgradeBlockItem(Block block, Settings settings, String tooltipString) {
		super(block, settings);
		this.tooltipString = tooltipString;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		tooltip.add(new TranslatableText("item.spectrum." + this.tooltipString + ".tooltip").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.upgrade.tooltip_increase").formatted(Formatting.GRAY));
	}
	
}
