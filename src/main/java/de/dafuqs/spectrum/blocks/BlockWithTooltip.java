package de.dafuqs.spectrum.blocks;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockWithTooltip extends Block {
	
	protected final Text tooltipText;
	
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
