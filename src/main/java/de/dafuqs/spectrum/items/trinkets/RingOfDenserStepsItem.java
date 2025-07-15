package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

import java.util.*;

public class RingOfDenserStepsItem extends GravityRingItem implements GravitableItem {
	
	public RingOfDenserStepsItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/ring_of_denser_steps"), InkColors.BROWN);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.ring_of_denser_steps.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.ring_of_denser_steps.tooltip2").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	public static ResourceLocation ATTRIBUTE_ID = SpectrumCommon.locate("ring_of_denser_steps_gravity");
	
	@Override
	protected ResourceLocation getAttributeID() {
		return ATTRIBUTE_ID;
	}
	
	@Override
	protected boolean negativeGravity() {
		return false;
	}
	
}
