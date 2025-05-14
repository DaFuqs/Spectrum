package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;

import java.util.*;

public class SuspiciousBrewItem extends BeverageItem {
	
	//TODO should this use the SuspiciousStewContents component instead?
	
	public SuspiciousBrewItem(Properties settings) {
		super(settings.component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
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
