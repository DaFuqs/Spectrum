package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.api.block.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class PedestalBlockItem extends BlockItem {
	
	private final PedestalVariant pedestalVariant;
	private final Component tooltipText;
	
	public PedestalBlockItem(Block block, Properties settings, PedestalVariant pedestalVariant, String tooltipTextString) {
		super(block, settings);
		this.pedestalVariant = pedestalVariant;
		this.tooltipText = Component.translatable(tooltipTextString);
	}
	
	public PedestalVariant getVariant() {
		return this.pedestalVariant;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(tooltipText);
	}
	
}
