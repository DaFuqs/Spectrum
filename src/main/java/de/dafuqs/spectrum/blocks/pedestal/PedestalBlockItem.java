package de.dafuqs.spectrum.blocks.pedestal;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class PedestalBlockItem extends BlockItem {
	
	private final PedestalVariant pedestalVariant;
	private final Text tooltipText;
	
	public PedestalBlockItem(Block block, Settings settings, PedestalVariant pedestalVariant, String tooltipTextString) {
		super(block, settings);
		this.pedestalVariant = pedestalVariant;
		this.tooltipText = new TranslatableText(tooltipTextString);
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
