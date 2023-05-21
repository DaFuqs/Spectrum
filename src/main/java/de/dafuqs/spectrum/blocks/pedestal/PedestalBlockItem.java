package de.dafuqs.spectrum.blocks.pedestal;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;

import java.util.*;

public class PedestalBlockItem extends BlockItem {
	
	private final PedestalVariant pedestalVariant;
	private final Text tooltipText;
	
	public PedestalBlockItem(Block block, Settings settings, PedestalVariant pedestalVariant, String tooltipTextString) {
		super(block, settings);
		this.pedestalVariant = pedestalVariant;
		this.tooltipText = Text.translatable(tooltipTextString);
	}
	
	public PedestalVariant getVariant() {
		return this.pedestalVariant;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(tooltipText);
	}
	
}
