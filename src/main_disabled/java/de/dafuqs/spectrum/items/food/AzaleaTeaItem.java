package de.dafuqs.spectrum.items.food;

import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;

import java.util.*;

public class AzaleaTeaItem extends DrinkItem {
	
	public AzaleaTeaItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.azalea_tea.tooltip").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
}
