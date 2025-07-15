package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.*;

import java.util.*;

public class NeatRingItem extends SpectrumTrinketItem {
	
	
	public NeatRingItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/neat_ring"));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.neat_ring.tooltip"));
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
	
}
