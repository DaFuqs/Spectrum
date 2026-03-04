package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class ThreatConfluxItem extends BlockItem {
	
	public ThreatConfluxItem(Block block, Item.Properties properties) {
		super(block, properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("block.spectrum.threat_conflux.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.spectrum.threat_conflux.tooltip2").withStyle(ChatFormatting.GRAY).append(SpectrumItems.MIDNIGHT_CHIP.get().getDefaultInstance().getDisplayName()));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
}
