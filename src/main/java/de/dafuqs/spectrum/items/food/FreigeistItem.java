package de.dafuqs.spectrum.items.food;

import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;

import java.util.*;

public class FreigeistItem extends DrinkItem {
	
	public FreigeistItem(Properties settings) {
		super(settings);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.freigeist.tooltip"));
	}
	
}
