package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;

import java.util.*;

public class SuspiciousBrewItem extends BeverageItem {
	
	public SuspiciousBrewItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		if (FermentedItem.isPreviewStack(stack)) {
			tooltip.add(Component.translatable("item.spectrum.suspicious_brew.tooltip.preview").withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable("item.spectrum.suspicious_brew.tooltip.preview2").withStyle(ChatFormatting.GRAY));
		}
	}
	
}
