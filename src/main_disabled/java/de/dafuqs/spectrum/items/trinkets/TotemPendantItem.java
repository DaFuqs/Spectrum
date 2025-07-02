package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;

import java.util.*;

public class TotemPendantItem extends SpectrumTrinketItem {
	
	public TotemPendantItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/totem_pendant"));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.totem_pendant.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	
}
