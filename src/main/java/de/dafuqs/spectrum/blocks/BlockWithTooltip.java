package de.dafuqs.spectrum.blocks;

import com.mojang.serialization.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class BlockWithTooltip extends Block {
	
	protected final Component tooltipText;
	
	public BlockWithTooltip(Properties settings, Component tooltipText) {
		super(settings);
		this.tooltipText = tooltipText;
	}

	@Override
	public MapCodec<? extends BlockWithTooltip> codec() {
		//TODO: Make the codec
		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(tooltipText);
	}
}
