package de.dafuqs.spectrum.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockWithTooltip extends Block {
	
	protected Text tooltipText;
	
	public BlockWithTooltip(Settings settings, Text tooltipText) {
		super(settings);
		this.tooltipText = tooltipText;
	}
	
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(tooltipText);
	}
}
